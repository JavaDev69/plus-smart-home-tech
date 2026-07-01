package ru.yandex.practicum.collector.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.collector.properties.KafkaProperties;

@Configuration
public class KafkaConfig {

    @Bean
    public Producer<String, Object> producerConfig(KafkaProperties properties) {
        return new KafkaProducer<>(properties.getProperties());
    }
}
