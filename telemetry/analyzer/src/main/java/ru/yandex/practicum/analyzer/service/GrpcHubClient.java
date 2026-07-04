package ru.yandex.practicum.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.dal.model.ScenarioAction;
import ru.yandex.practicum.analyzer.mapper.DeviceActionMapper;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;

@Slf4j
@Component
public class GrpcHubClient {
    private final DeviceActionMapper mapper;
    private final HubRouterControllerBlockingStub hubRouterClient;

    public GrpcHubClient(@GrpcClient("hub-router") HubRouterControllerBlockingStub hubRouterClient,
                         DeviceActionMapper mapper) {
        this.hubRouterClient = hubRouterClient;
        this.mapper = mapper;
    }

    public void sendAction(ScenarioAction action) {
        log.debug("Sending action {}", action);
        try {
            DeviceActionRequest actionRequest = mapper.map(action);
            hubRouterClient.handleDeviceAction(actionRequest);
            log.debug("Action sent");
        } catch (Exception e) {
            log.error("Error sending action {}", action, e);
        }
    }

}
