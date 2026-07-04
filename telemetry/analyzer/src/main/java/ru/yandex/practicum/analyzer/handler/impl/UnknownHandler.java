package ru.yandex.practicum.analyzer.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import ru.yandex.practicum.analyzer.handler.HubEventHandler;
import ru.yandex.practicum.analyzer.handler.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Slf4j
public class UnknownHandler implements SnapshotHandler, HubEventHandler {
    @Override
    public void handle(HubEventAvro event) {
        log.warn("Unknown event received: {}", event);
        throw new IllegalArgumentException("Отсутствует обработчик для события " + event.getSchema());
    }

    @Override
    public Schema getEventSchema() {
        return null;
    }

    @Override
    public void handle(SensorsSnapshotAvro snapshotAvro) {
        log.warn("Unknown snapshot received: {}", snapshotAvro);
        throw new IllegalArgumentException("Отсутствует обработчик для snapshot " + snapshotAvro.getSchema());
    }
}
