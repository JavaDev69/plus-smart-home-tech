package ru.yandex.practicum.analyzer.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Properties;

@Getter
@Setter
@Validated
@ConfigurationProperties("analyzer.kafka.hub-event-consumer")
public class HubEventConsumerProp {
    private Properties properties;
    @NotBlank
    private String topic;
    private long pollTimeoutMs;
}
