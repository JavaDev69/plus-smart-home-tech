package ru.yandex.practicum.collector.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.mapper.EventProtoMapper;
import ru.yandex.practicum.collector.service.EventService;
import ru.yandex.practicum.dto.sensor.SwitchSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Log
@Component
@RequiredArgsConstructor
public class SwitchSensorEventHandler implements SensorEventHandler {
    private final EventService eventService;
    private final EventProtoMapper mapper;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        log.info("SWITCH_SENSOR event received: " + event);
        SwitchSensorEvent mappedEvent = mapper.mapToSwitchEvent(event);
        eventService.collectSensorEvent(mappedEvent);
    }
}
