package ru.yandex.practicum.collector.kafka;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.serialization.EventSerializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Properties;

@Component
public class KafkaAvroProducer {
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
        ProducerRecord<String, Object> record = new ProducerRecord<>(EventTopics.SENSOR_TOPIC_V1, event);
        producer.send(record);
    }

    public void send(HubEventAvro event) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(EventTopics.HUB_TOPIC_V1, event);
        producer.send(record);
    }
}
