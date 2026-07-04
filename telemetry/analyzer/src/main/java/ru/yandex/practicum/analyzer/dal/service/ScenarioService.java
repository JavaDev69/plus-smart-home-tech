package ru.yandex.practicum.analyzer.dal.service;

import ru.yandex.practicum.analyzer.dal.model.Scenario;
import ru.yandex.practicum.dto.hub.ScenarioAddedEvent;

import java.util.Collection;
import java.util.Optional;

public interface ScenarioService {
    void save(ScenarioAddedEvent scenario);

    void delete(Scenario scenario);

    Collection<Scenario> getScenarios();

    Collection<Scenario> getScenariosByHubId(String hubId);

    Optional<Scenario> getScenarioByHubIdAndName(String hubId, String name);
}
