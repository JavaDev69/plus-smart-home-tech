package ru.yandex.practicum.analyzer.dal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.dal.model.*;
import ru.yandex.practicum.analyzer.dal.repository.*;
import ru.yandex.practicum.analyzer.dal.service.ScenarioService;
import ru.yandex.practicum.dto.hub.ScenarioAddedEvent;
import ru.yandex.practicum.dto.scenario.DeviceAction;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScenarioServiceImpl implements ScenarioService {
    private final ScenarioConditionRepository scenarioConditionRepository;
    private final ScenarioRepository scenarioRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final SensorRepository sensorRepository;
    private final ScenarioActionRepository scenarioActionRepository;

    @Transactional
    @Override
    public void save(ScenarioAddedEvent event) {
        log.debug("Saving scenario: {}", event);

        Scenario scenario = getScenarioByHubIdAndName(event.getHubId(), event.getName())
                .orElseGet(() ->
                        scenarioRepository.save(Scenario.builder()
                                .name(event.getName())
                                .hubId(event.getHubId())
                                .build()));
        removeExistingActionAndConditions(scenario);
        saveActions(event.getActions(), scenario);
        saveConditions(event.getConditions(), scenario);

        log.debug("Saved scenario: {}", event);
    }

    private void removeExistingActionAndConditions(Scenario scenario) {
        scenarioActionRepository.deleteByScenario(scenario);
        scenarioConditionRepository.deleteByScenario(scenario);
    }

    private Sensor getSensor(String id) {
        return sensorRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor with id " + id + " not found"));
    }

    private void saveConditions(
            Collection<ru.yandex.practicum.dto.scenario.ScenarioCondition> conditions,
            Scenario scenario) {
        log.debug("Saving conditions for scenario: {}", scenario);
        conditions
                .forEach(e -> {
                    Sensor sensor = getSensor(e.getSensorId());

                    Condition condition = conditionRepository.save(Condition.builder()
                            .type(e.getType())
                            .operation(e.getOperation())
                            .value(e.getValue())
                            .build());

                    scenarioConditionRepository.save(
                            ScenarioCondition.builder()
                                    .id(ScenarioConditionId.builder()
                                            .conditionId(condition.getId())
                                            .scenarioId(scenario.getId())
                                            .sensorId(sensor.getId())
                                            .build())
                                    .condition(condition)
                                    .sensor(sensor)
                                    .scenario(scenario)
                                    .build()
                    );
                });
        log.debug("Saved scenario conditions: {}", conditions);
    }

    private void saveActions(Collection<DeviceAction> actions, Scenario scenario) {
        log.info("Saving actions for scenario: {}", scenario.getId());
        actions
                .forEach(e -> {
                    Sensor sensor = getSensor(e.getSensorId());

                    Action action = actionRepository.save(
                            Action.builder()
                                    .type(e.getType())
                                    .value(e.getValue())
                                    .build());

                    scenarioActionRepository.save(
                            ScenarioAction.builder()
                                    .id(ScenarioActionId.builder()
                                            .actionId(action.getId())
                                            .scenarioId(scenario.getId())
                                            .sensorId(sensor.getId())
                                            .build())
                                    .scenario(scenario)
                                    .action(action)
                                    .sensor(sensor)
                                    .build());
                });
        log.debug("Saved scenario actions: {}", actions);
    }

    @Transactional
    @Override
    public void delete(Scenario scenario) {
        log.debug("Deleting scenario: {}", scenario);

        getScenarioByHubIdAndName(scenario.getHubId(), scenario.getName())
                .ifPresentOrElse(e -> {
                            removeExistingActionAndConditions(e);
                            scenarioRepository.delete(e);
                        },
                        () -> log.debug("Scenario {} not found", scenario));

        log.debug("Deleted scenario: {}", scenario);
    }

    @Override
    public Collection<Scenario> getScenarios() {
        return scenarioRepository.findAll();
    }

    @Override
    public Collection<Scenario> getScenariosByHubId(String hubId) {
        return scenarioRepository.findByHubId(hubId);
    }

    @Override
    public Optional<Scenario> getScenarioByHubIdAndName(String hubId, String name) {
        return scenarioRepository.findByHubIdAndName(hubId, name);
    }
}
