package ru.yandex.practicum.aggregator.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.aggregator.properties.ConsumerProperties;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaConsumer<String, SensorEventAvro> consumer(ConsumerProperties properties) {
        return new KafkaConsumer<>(properties.getProperties());
    }
}
