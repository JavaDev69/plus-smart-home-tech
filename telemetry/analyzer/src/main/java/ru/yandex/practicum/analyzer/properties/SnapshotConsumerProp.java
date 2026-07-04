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
@ConfigurationProperties("analyzer.kafka.snapshot-consumer")
public class SnapshotConsumerProp {
    private Properties properties;
    @NotBlank
    private String topic;
    private long pollTimeoutMs;
}
