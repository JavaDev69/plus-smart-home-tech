package ru.yandex.practicum.analyzer.processor;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.handler.SnapshotHandler;
import ru.yandex.practicum.analyzer.handler.impl.UnknownHandler;
import ru.yandex.practicum.analyzer.properties.SnapshotConsumerProp;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SnapshotProcessor extends AbstractProcessor<SensorsSnapshotAvro> {
    private final SnapshotConsumerProp properties;
    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final Map<Schema, SnapshotHandler> snapshotHandlers;

    public SnapshotProcessor(SnapshotConsumerProp properties,
                             @Qualifier("snapshotConsumer") KafkaConsumer<String, SensorsSnapshotAvro> consumer,
                             Set<SnapshotHandler> snapshotHandlers) {
        this.properties = properties;
        this.consumer = consumer;
        this.snapshotHandlers = snapshotHandlers.stream()
                .collect(Collectors.toMap(SnapshotHandler::getEventSchema, Function.identity()));
    }

    @PreDestroy
    public void shutdown() {
        consumer.wakeup();
    }

    @Override
    public void run() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(Collections.singletonList(properties.getTopic()));

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records =
                        consumer.poll(Duration.ofMillis(properties.getPollTimeoutMs()));

                int count = 0;
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    handleRecord(record);
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitAsync();
            }
        } catch (WakeupException e) {
            // игнорируем - закрываем консьюмер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от агрегатора", e);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                log.info("Закрываем Snapshot консьюмер");
                consumer.close();
            }
        }
    }

    private void handleRecord(ConsumerRecord<String, SensorsSnapshotAvro> record) {
        SensorsSnapshotAvro snapshot = record.value();
        log.debug("Получен снапшот: {}", snapshot);
        SnapshotHandler handler = snapshotHandlers.getOrDefault(snapshot.getSchema(), new UnknownHandler());
        log.debug("Handler found: {}", handler.getClass().getSimpleName());
        handler.handle(snapshot);
        log.debug("Обработан снапшот: {}", snapshot);
    }

}
