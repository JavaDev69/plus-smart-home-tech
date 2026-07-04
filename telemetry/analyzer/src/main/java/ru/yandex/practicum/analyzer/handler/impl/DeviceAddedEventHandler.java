package ru.yandex.practicum.analyzer.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.dal.model.Sensor;
import ru.yandex.practicum.analyzer.dal.service.SensorService;
import ru.yandex.practicum.analyzer.handler.HubEventHandler;
import ru.yandex.practicum.analyzer.mapper.HubEventMapper;
import ru.yandex.practicum.dto.hub.DeviceAddedEvent;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAddedEventHandler implements HubEventHandler {
    private final HubEventMapper mapper;
    private final SensorService service;

    @Override
    public Schema getEventSchema() {
        return DeviceAddedEventAvro.getClassSchema();
    }

    @Override
    public void handle(HubEventAvro event) {
        log.info("DEVICE_ADDED event received: {}", event);
        DeviceAddedEvent mappedEvent = mapper.mapToDeviceAdded(event);

        Sensor sensor = Sensor.builder()
                .id(mappedEvent.getId())
                .hubId(mappedEvent.getHubId())
                .build();

        service.save(sensor);
    }
}
