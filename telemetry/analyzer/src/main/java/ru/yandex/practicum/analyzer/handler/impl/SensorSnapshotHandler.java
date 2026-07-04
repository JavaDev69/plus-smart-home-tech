package ru.yandex.practicum.analyzer.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.dal.service.SnapshotService;
import ru.yandex.practicum.analyzer.handler.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorSnapshotHandler implements SnapshotHandler {
    private final SnapshotService service;

    @Override
    public Schema getEventSchema() {
        return SensorsSnapshotAvro.getClassSchema();
    }

    @Override
    public void handle(SensorsSnapshotAvro snapshot) {

        log.info("SNAPSHOT event received: {}", snapshot);
        service.processSnapshot(snapshot);
    }
}
