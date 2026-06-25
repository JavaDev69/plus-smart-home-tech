package ru.yandex.practicum.collector.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.dto.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.collector.mapper.EventProtoMapper;
import ru.yandex.practicum.collector.service.EventService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Log
@Component
@RequiredArgsConstructor
public class TemperatureSensorEventHandler implements SensorEventHandler {
    private final EventService eventService;
    private final EventProtoMapper mapper;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        log.info("TEMPERATURE_SENSOR event received: " + event);
        TemperatureSensorEvent mappedEvent = mapper.mapToTemperatureEvent(event);
        eventService.collectSensorEvent(mappedEvent);
    }
}
