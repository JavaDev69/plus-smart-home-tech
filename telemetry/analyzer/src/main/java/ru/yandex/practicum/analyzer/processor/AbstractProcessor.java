package ru.yandex.practicum.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractProcessor<T extends SpecificRecordBase> implements Runnable {
    protected final Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

    protected void manageOffsets(
            ConsumerRecord<String, T> record,
            int count,
            Consumer<String, T> consumer) {
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
