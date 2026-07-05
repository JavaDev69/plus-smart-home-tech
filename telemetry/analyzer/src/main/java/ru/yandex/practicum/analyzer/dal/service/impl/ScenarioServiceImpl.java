package ru.yandex.practicum.analyzer.dal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.dal.model.Action;
import ru.yandex.practicum.analyzer.dal.model.Condition;
import ru.yandex.practicum.analyzer.dal.model.Scenario;
import ru.yandex.practicum.analyzer.dal.model.ScenarioAction;
import ru.yandex.practicum.analyzer.dal.model.ScenarioActionId;
import ru.yandex.practicum.analyzer.dal.model.ScenarioCondition;
import ru.yandex.practicum.analyzer.dal.model.ScenarioConditionId;
import ru.yandex.practicum.analyzer.dal.model.Sensor;
import ru.yandex.practicum.analyzer.dal.repository.ActionRepository;
import ru.yandex.practicum.analyzer.dal.repository.ConditionRepository;
import ru.yandex.practicum.analyzer.dal.repository.ScenarioActionRepository;
import ru.yandex.practicum.analyzer.dal.repository.ScenarioConditionRepository;
import ru.yandex.practicum.analyzer.dal.repository.ScenarioRepository;
import ru.yandex.practicum.analyzer.dal.repository.SensorRepository;
import ru.yandex.practicum.analyzer.dal.service.ScenarioService;
import ru.yandex.practicum.dto.HasSensorId;
import ru.yandex.practicum.dto.hub.ScenarioAddedEvent;
import ru.yandex.practicum.dto.scenario.DeviceAction;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        saveDeviceActions(event.getActions(), scenario);
        saveScenarioConditions(event.getConditions(), scenario);

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

    private Map<String, Sensor> getSensorsByIds(Collection<? extends HasSensorId> hasSensorIds) {
        List<String> sensorIds = hasSensorIds.stream()
                .map(HasSensorId::getSensorId)
                .toList();
        return sensorRepository.findAllByIdIsIn(sensorIds)
                .stream()
                .collect(Collectors.toMap(Sensor::getId, Function.identity()));
    }

    private Map<String, Condition> saveConditions(Collection<ru.yandex.practicum.dto.scenario.ScenarioCondition> conditions) {
        Map<String, Condition> conditionsToSave = conditions.stream()
                .map(sc -> {
                    Condition condition = Condition.builder()
                            .type(sc.getType())
                            .operation(sc.getOperation())
                            .value(sc.getValue())
                            .build();
                    return Map.entry(sc.getSensorId(), condition);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        conditionRepository.saveAll(conditionsToSave.values());
        return conditionsToSave;
    }

    private void saveScenarioConditions(
            Collection<ru.yandex.practicum.dto.scenario.ScenarioCondition> scenarioConditions,
            Scenario scenario) {
        log.debug("Saving scenarioConditions for scenario: {}", scenario);

        Map<String, Sensor> sensors = getSensorsByIds(scenarioConditions);

        Map<String, Condition> conditions = saveConditions(scenarioConditions);

        List<ScenarioCondition> scToSave = scenarioConditions.stream()
                .map(e -> {
                    Sensor sensor = sensors.get(e.getSensorId());

                    Condition condition = conditions.get(e.getSensorId());

                    return ScenarioCondition.builder()
                            .id(ScenarioConditionId.builder()
                                    .conditionId(condition.getId())
                                    .scenarioId(scenario.getId())
                                    .sensorId(sensor.getId())
                                    .build())
                            .condition(condition)
                            .sensor(sensor)
                            .scenario(scenario)
                            .build();
                })
                .toList();

        List<ScenarioCondition> saved = scenarioConditionRepository.saveAll(scToSave);
        log.debug("Saved scenario scenarioConditions: {}", saved);
    }

    private Map<String, Action> saveActions(Collection<DeviceAction> deviceActions) {
        Map<String, Action> actionsToSave = deviceActions.stream()
                .map(sc -> {
                    Action action = Action.builder()
                            .type(sc.getType())
                            .value(sc.getValue())
                            .build();
                    return Map.entry(sc.getSensorId(), action);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        actionRepository.saveAll(actionsToSave.values());
        return actionsToSave;
    }

    private void saveDeviceActions(Collection<DeviceAction> deviceActions, Scenario scenario) {
        log.info("Saving actions for scenario: {}", scenario.getId());
        Map<String, Sensor> sensors = getSensorsByIds(deviceActions);
        Map<String, Action> actions = saveActions(deviceActions);

        List<ScenarioAction> saToSave = deviceActions.stream()
                .map(e -> {
                    Sensor sensor = sensors.get(e.getSensorId());
                    Action action = actions.get(e.getSensorId());

                    return ScenarioAction.builder()
                            .id(ScenarioActionId.builder()
                                    .actionId(action.getId())
                                    .scenarioId(scenario.getId())
                                    .sensorId(sensor.getId())
                                    .build())
                            .scenario(scenario)
                            .action(action)
                            .sensor(sensor)
                            .build();
                })
                .toList();

        List<ScenarioAction> saved = scenarioActionRepository.saveAll(saToSave);
        log.debug("Saved scenario actions: {}", saved);
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