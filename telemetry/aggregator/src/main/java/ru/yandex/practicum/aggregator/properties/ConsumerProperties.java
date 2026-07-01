package ru.yandex.practicum.aggregator.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Properties;

@Getter
@Setter
@Validated
@ConfigurationProperties("aggregator.kafka.consumer")
public class ConsumerProperties {
    private Properties properties;
    private String topic;
    private long pollTimeoutMs;
}
