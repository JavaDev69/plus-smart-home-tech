package ru.yandex.practicum.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.aggregator.handler.EventHandler;
import ru.yandex.practicum.aggregator.properties.ConsumerProperties;
import ru.yandex.practicum.aggregator.properties.ProducerProperties;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс AggregationStarter, ответственный за запуск агрегации данных.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final ProducerProperties producerProperties;
    private final Producer<String, SensorsSnapshotAvro> producer;
    private final EventHandler eventHandler;
    private final ConsumerProperties consumerProperties;
    private final Consumer<String, SensorEventAvro> consumer;
    private final Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(consumerProperties.getTopic()));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records =
                        consumer.poll(Duration.ofMillis(consumerProperties.getPollTimeoutMs()));

                int count = 0;
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    log.debug("Получено событие: {}", record.value());
                    updateState(record);
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitAsync();
            }

        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    private void updateState(ConsumerRecord<String, SensorEventAvro> record) {
        eventHandler
                .handleRecord(record)
                .ifPresentOrElse(
                        snapshot -> {
                            log.debug("Есть изменение, запись snapshot'a в топик: {}", snapshot);
                            ProducerRecord<String, SensorsSnapshotAvro> pr = producerRecordFromSnapshot(snapshot);
                            producer.send(pr);
                            log.debug("Snapshot отправлен.");
                        },
                        () -> log.debug("Изменений нет, snapshot не обновлен.")
                );
    }

    private ProducerRecord<String, SensorsSnapshotAvro> producerRecordFromSnapshot(SensorsSnapshotAvro snapshot) {
        return new ProducerRecord<>(
                producerProperties.getTopic(),
                null,
                snapshot.getTimestamp().toEpochMilli(),
                snapshot.getHubId(),
                snapshot);
    }

    private void manageOffsets(
            ConsumerRecord<String, SensorEventAvro> record,
            int count,
            Consumer<String, SensorEventAvro> consumer) {
        offsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1));

        if (count % 10 == 0) {
            log.debug("Начата фиксация смещения: {}", record.offset());

            consumer.commitAsync(offsets, (offset, exception) -> {
                if (exception != null) {
                    log.warn("Во время фиксации произошла ошибка. Смещение: {}", offset, exception);
                }
            });
        }
    }
}
