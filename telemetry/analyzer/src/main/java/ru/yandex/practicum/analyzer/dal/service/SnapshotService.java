package ru.yandex.practicum.analyzer.dal.service;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotService {
    void processSnapshot(SensorsSnapshotAvro snapshot);
}
