package ru.yandex.practicum.collector.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.mapper.EventProtoMapper;
import ru.yandex.practicum.collector.service.EventService;
import ru.yandex.practicum.dto.hub.DeviceAddedEvent;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

@Log
@Component
@RequiredArgsConstructor
public class DeviceAddedEventHandler implements HubEventHandler {
    private final EventService eventService;
    private final EventProtoMapper mapper;

    @Override
    public HubEventProto.PayloadCase getEventType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        log.info("DEVICE_ADDED event received: " + event);
        DeviceAddedEvent mappedEvent = mapper.mapToDeviceAdded(event);
        eventService.collectHubEvent(mappedEvent);
    }
}
