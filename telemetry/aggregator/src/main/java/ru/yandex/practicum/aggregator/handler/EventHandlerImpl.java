package ru.yandex.practicum.aggregator.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventHandlerImpl implements EventHandler {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    @Override
    public Optional<SensorsSnapshotAvro> handleRecord(ConsumerRecord<String, SensorEventAvro> record) {
        log.debug("Начата обработка события: {}", record.value());

        SensorEventAvro value = record.value();

        SensorsSnapshotAvro snapshot = snapshots.getOrDefault(value.getHubId(), createNewSnapshot(value));
        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();
        SensorStateAvro oldState = sensorsState.get(value.getId());

        if (oldState != null
                &&
                (value.getTimestamp().isBefore(oldState.getTimestamp())
                        || value.getPayload().equals(oldState.getData()))) {
            log.debug("Закончена обработка события, изменений нет: {}", record.value());
            return Optional.empty();
        }

        SensorStateAvro newState = createNewState(value);
        sensorsState.put(value.getId(), newState);
        snapshot.setTimestamp(value.getTimestamp());
        snapshots.put(value.getHubId(), snapshot);
        log.debug("Закончена обработка события, есть изменения: {}", record.value());

        return Optional.of(snapshot);
    }

    private SensorsSnapshotAvro createNewSnapshot(SensorEventAvro event) {
        return SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setSensorsState(new HashMap<>())
                .build();
    }

    private SensorStateAvro createNewState(SensorEventAvro event) {
        return SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
    }
}
