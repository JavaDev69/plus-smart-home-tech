package ru.yandex.practicum.aggregator.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.aggregator.properties.ConsumerProperties;
import ru.yandex.practicum.aggregator.properties.ProducerProperties;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Configuration
public class KafkaConfig {
    @Bean
    public KafkaProducer<String, SensorsSnapshotAvro> producer(ProducerProperties properties) {
        return new KafkaProducer<>(properties.getProperties());
    }

    @Bean
    public KafkaConsumer<String, SensorEventAvro> consumer(ConsumerProperties properties) {
        return new KafkaConsumer<>(properties.getProperties());
    }
}
