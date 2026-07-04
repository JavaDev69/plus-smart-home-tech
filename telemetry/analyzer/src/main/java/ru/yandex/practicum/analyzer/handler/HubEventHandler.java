package ru.yandex.practicum.analyzer.handler;

import org.apache.avro.Schema;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {
    Schema getEventSchema();

    void handle(HubEventAvro event);
}
