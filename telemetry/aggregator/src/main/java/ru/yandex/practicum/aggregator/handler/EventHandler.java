package ru.yandex.practicum.aggregator.handler;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;

public interface EventHandler {
    Optional<SensorsSnapshotAvro> handleRecord(ConsumerRecord<String, SensorEventAvro> record);
}
