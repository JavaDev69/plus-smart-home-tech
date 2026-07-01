package ru.yandex.practicum.collector.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Properties;

@Getter
@Setter
@Validated
@ConfigurationProperties("collector.kafka.producer")
public class KafkaProperties {
    private Properties properties;
    @Valid
    private Topics topics;

    public record Topics(
            @NotBlank
            String sensorsEvents,
            @NotBlank
            String hubsEvents
    ) {
    }
}
