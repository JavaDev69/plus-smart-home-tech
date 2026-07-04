package ru.yandex.practicum.analyzer.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.analyzer.properties.HubEventConsumerProp;
import ru.yandex.practicum.analyzer.properties.SnapshotConsumerProp;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Configuration
public class KafkaConfig {
    @Bean("hubEventConsumer")
    public KafkaConsumer<String, HubEventAvro> hubEventConsumer(HubEventConsumerProp properties) {
        return new KafkaConsumer<>(properties.getProperties());
    }

    @Bean("snapshotConsumer")
    public KafkaConsumer<String, SensorsSnapshotAvro> snapshotConsumer(SnapshotConsumerProp properties) {
        return new KafkaConsumer<>(properties.getProperties());
    }
}
