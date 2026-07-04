package ru.yandex.practicum.analyzer.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.dal.service.ScenarioService;
import ru.yandex.practicum.analyzer.handler.HubEventHandler;
import ru.yandex.practicum.analyzer.mapper.HubEventMapper;
import ru.yandex.practicum.dto.hub.ScenarioAddedEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {
    private final HubEventMapper mapper;
    private final ScenarioService service;

    @Override
    public Schema getEventSchema() {
        return ScenarioAddedEventAvro.getClassSchema();
    }

    @Override
    public void handle(HubEventAvro event) {
        log.info("SCENARIO_ADDED event received: {}", event);
        ScenarioAddedEvent mappedEvent = mapper.mapToScenarioAdded(event);
        service.save(mappedEvent);
    }
}
