package ru.yandex.practicum.aggregator.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.aggregator.properties.ProducerProperties;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.concurrent.Future;

/**
 * @author Andrew Vilkov
 * @created 05.07.2026 - 14:33
 * @project plus-smart-home-tech
 */

@Component
public class KafkaEventProducer implements AutoCloseable {
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private static final int TIMEOUT_SECONDS = 10;

    public KafkaEventProducer(ProducerProperties properties) {
        this.producer = new KafkaProducer<>(properties.getProperties());
    }

    public void flush() {
        producer.flush();
    }

    public void close(Duration timeout) {
        producer.close(timeout);
    }

    public Future<RecordMetadata> send(ProducerRecord<String, SensorsSnapshotAvro> pr) {
        return producer.send(pr);
    }

    @Override
    public void close() {
        producer.flush();
        producer.close(Duration.ofSeconds(TIMEOUT_SECONDS));
    }
}
