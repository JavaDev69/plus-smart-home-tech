package ru.yandex.practicum.analyzer.processor;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.handler.HubEventHandler;
import ru.yandex.practicum.analyzer.handler.impl.UnknownHandler;
import ru.yandex.practicum.analyzer.properties.HubEventConsumerProp;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HubEventProcessor extends AbstractProcessor<HubEventAvro> {
    private final HubEventConsumerProp properties;
    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final Map<Schema, HubEventHandler> hubEventHandlers;

    public HubEventProcessor(HubEventConsumerProp properties,
                             @Qualifier("hubEventConsumer") KafkaConsumer<String, HubEventAvro> consumer,
                             Set<HubEventHandler> hubEventHandlers) {
        this.properties = properties;
        this.consumer = consumer;
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getEventSchema, Function.identity()));
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
                ConsumerRecords<String, HubEventAvro> records =
                        consumer.poll(Duration.ofMillis(properties.getPollTimeoutMs()));

                int count = 0;
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    handleRecord(record);
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitSync(offsets);
            }
        } catch (WakeupException e) {
            // игнорируем - закрываем консьюмер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от хаба", e);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                log.info("Закрываем HubEvent консьюмер");
                consumer.close();
            }
        }
    }

    private void handleRecord(ConsumerRecord<String, HubEventAvro> record) {
        HubEventAvro eventAvro = record.value();
        log.debug("Получено событие: {}", eventAvro);
        SpecificRecordBase payload = (SpecificRecordBase) eventAvro.getPayload();
        HubEventHandler handler = hubEventHandlers.getOrDefault(payload.getSchema(), new UnknownHandler());
        log.debug("Handler found: {}", handler.getClass().getSimpleName());
        handler.handle(eventAvro);
        log.debug("Обработано событие: {}", eventAvro);
    }

}
