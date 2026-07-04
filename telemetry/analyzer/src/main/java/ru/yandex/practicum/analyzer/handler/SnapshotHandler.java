package ru.yandex.practicum.analyzer.handler;

import org.apache.avro.Schema;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotHandler {
    Schema getEventSchema();

    void handle(SensorsSnapshotAvro snapshotAvro);
}
