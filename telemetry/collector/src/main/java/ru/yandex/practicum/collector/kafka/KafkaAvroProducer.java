package ru.yandex.practicum.collector.kafka;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.serialization.EventSerializer;

import java.time.Duration;
import java.util.Properties;

import static ru.yandex.practicum.collector.kafka.EventTopics.HUB_TOPIC_V1;
import static ru.yandex.practicum.collector.kafka.EventTopics.SENSOR_TOPIC_V1;

@Component
public class KafkaAvroProducer implements AutoCloseable {
    private Producer<String, Object> producer;

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EventSerializer.class);
        producer = new KafkaProducer<>(props);
    }

    public void send(SensorEventAvro event) {
        ProducerRecord<String, Object> record =
                new ProducerRecord<>(
                        SENSOR_TOPIC_V1,
                        null,
                        event.getTimestamp().toEpochMilli(),
                        null,
                        event);
        producer.send(record);
    }

    public void send(HubEventAvro event) {
        ProducerRecord<String, Object> record =
                new ProducerRecord<>(
                        HUB_TOPIC_V1,
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
