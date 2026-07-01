package ru.yandex.practicum.collector.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.properties.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class KafkaAvroProducer implements AutoCloseable {
    private final Producer<String, Object> producer;
    private final KafkaProperties kafkaProperties;

    public void send(SensorEventAvro event) {
        ProducerRecord<String, Object> record =
                new ProducerRecord<>(
                        kafkaProperties.getTopics().sensorsEvents(),
                        null,
                        event.getTimestamp().toEpochMilli(),
                        null,
                        event);
        producer.send(record);
    }

    public void send(HubEventAvro event) {
        ProducerRecord<String, Object> record =
                new ProducerRecord<>(
                        kafkaProperties.getTopics().hubsEvents(),
                        null,
                        event.getTimestamp().toEpochMilli(),
                        null, event);
        producer.send(record);
    }

    @Override
    public void close() {
        producer.flush();
        producer.close(Duration.ofSeconds(10));
    }
}
