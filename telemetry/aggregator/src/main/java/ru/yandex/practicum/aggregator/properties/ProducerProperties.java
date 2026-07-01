package ru.yandex.practicum.aggregator.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@Getter
@Setter
@ConfigurationProperties("aggregator.kafka.producer")
public class ProducerProperties {
    private Properties properties;
    private String topic;
}
