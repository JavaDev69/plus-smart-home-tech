package ru.yandex.practicum.analyzer.dal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.dal.model.Condition;
import ru.yandex.practicum.analyzer.dal.model.Scenario;
import ru.yandex.practicum.analyzer.dal.model.ScenarioAction;
import ru.yandex.practicum.analyzer.dal.model.ScenarioCondition;
import ru.yandex.practicum.analyzer.dal.repository.ScenarioActionRepository;
import ru.yandex.practicum.analyzer.dal.repository.ScenarioConditionRepository;
import ru.yandex.practicum.analyzer.dal.repository.ScenarioRepository;
import ru.yandex.practicum.analyzer.dal.service.SnapshotService;
import ru.yandex.practicum.analyzer.service.GrpcHubClient;
import ru.yandex.practicum.dto.scenario.ScenarioConditionType;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SnapshotServiceImpl implements SnapshotService {
    private final ScenarioRepository scenarioRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;
    private final ScenarioActionRepository scenarioActionRepository;
    private final GrpcHubClient hubClient;

    @Override
    public void processSnapshot(SensorsSnapshotAvro snapshot) {
        log.debug("Process snapshot {}", snapshot);
        List<Scenario> scenarios = scenarioRepository.findByHubId(snapshot.getHubId());
        if (scenarios.isEmpty()) {
            return;
        }

        Map<Long, List<ScenarioCondition>> conditionsByScenarioId = scenarioConditionRepository.findByScenarioIn(scenarios).stream()
                .collect(Collectors.groupingBy(e -> e.getScenario().getId()));

        List<Scenario> matchedScenarios = scenarios.stream()
                .filter(e -> scenarioFilter(e, snapshot.getSensorsState(), conditionsByScenarioId))
                .toList();
        if (matchedScenarios.isEmpty()) {
            return;
        }

        Map<Long, List<ScenarioAction>> actionsByScenarioId = scenarioActionRepository.findByScenarioIn(matchedScenarios).stream()
                .collect(Collectors.groupingBy(e -> e.getScenario().getId()));

        matchedScenarios.stream()
                .map(e -> actionsByScenarioId.getOrDefault(e.getId(), Collections.emptyList()))
                .flatMap(List::stream)
                .forEach(hubClient::sendAction);
    }

    private boolean scenarioFilter(Scenario scenario,
                                   Map<String, SensorStateAvro> sensorsState,
                                   Map<Long, List<ScenarioCondition>> conditionsByScenarioId) {
        List<ScenarioCondition> conditions = conditionsByScenarioId.getOrDefault(scenario.getId(), Collections.emptyList());

        return conditions.stream()
                .allMatch(e -> {
                    Condition condition = e.getCondition();
                    SensorStateAvro sensorStateAvro = sensorsState.get(e.getSensor().getId());
                    if (sensorStateAvro == null) {
                        return false;
                    }

                    Integer actualValue = getValue(condition.getType(), sensorStateAvro);
                    if (actualValue == null) {
                        log.warn("Actual value for sensor {} is null.", sensorStateAvro.getData());
                        return false;
                    }
                    return condition.getOperation().test(actualValue, condition.getValue());
                });
    }

    private Integer getValue(ScenarioConditionType type, SensorStateAvro sensorState) {
        Object data = sensorState.getData();
        String name = data.getClass().getSimpleName();
        switch (name) {
            case "SwitchSensorAvro":
                return ((SwitchSensorAvro) data).getState() ? 1 : 0;
            case "MotionSensorAvro":
                return ((MotionSensorAvro) data).getMotion() ? 1 : 0;
            case "TemperatureSensorAvro":
                return ((TemperatureSensorAvro) data).getTemperatureC();
            case "LightSensorAvro":
                return ((LightSensorAvro) data).getLuminosity();
            case "ClimateSensorAvro":
                ClimateSensorAvro sensor = (ClimateSensorAvro) data;
                return switch (type) {
                    case TEMPERATURE -> sensor.getTemperatureC();
                    case HUMIDITY -> sensor.getHumidity();
                    case CO2LEVEL -> sensor.getCo2Level();
                    default -> {
                        log.warn("Unknown condition type {} for Climate sensor", type);
                        yield null;
                    }
                };
            default:
                log.warn("Unknown sensor type {}", type);
                return null;
        }
    }
}
