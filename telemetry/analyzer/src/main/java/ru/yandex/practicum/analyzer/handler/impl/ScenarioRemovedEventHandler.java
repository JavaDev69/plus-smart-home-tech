package ru.yandex.practicum.analyzer.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.dal.model.Scenario;
import ru.yandex.practicum.analyzer.dal.service.ScenarioService;
import ru.yandex.practicum.analyzer.handler.HubEventHandler;
import ru.yandex.practicum.analyzer.mapper.HubEventMapper;
import ru.yandex.practicum.dto.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioRemovedEventHandler implements HubEventHandler {
    private final HubEventMapper mapper;
    private final ScenarioService service;

    @Override
    public Schema getEventSchema() {
        return ScenarioRemovedEventAvro.getClassSchema();
    }

    @Override
    public void handle(HubEventAvro event) {

        log.info("SCENARIO_REMOVED event received: {}", event);
        ScenarioRemovedEvent mappedEvent = mapper.mapToScenarioRemoved(event);
        Optional<Scenario> scenario = service.getScenarioByHubIdAndName(mappedEvent.getHubId(), mappedEvent.getName());
        scenario.ifPresent(service::delete);
    }
}
