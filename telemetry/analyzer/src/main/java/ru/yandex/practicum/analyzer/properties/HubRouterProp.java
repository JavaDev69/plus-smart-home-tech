package ru.yandex.practicum.analyzer.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties("analyzer.grpc.client.hub-router")
public class HubRouterProp {
    private String address;
    private Boolean enableKeepAlive;
    private Boolean keepAliveWithoutCalls;
    private String negotiationType;
}
