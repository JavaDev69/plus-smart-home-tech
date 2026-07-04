package ru.yandex.practicum.analyzer.dal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.dal.model.Condition;
import ru.yandex.practicum.analyzer.dal.model.Scenario;
import ru.yandex.practicum.analyzer.dal.model.ScenarioCondition;
import ru.yandex.practicum.analyzer.dal.repository.ScenarioActionRepository;
import ru.yandex.practicum.analyzer.dal.repository.ScenarioConditionRepository;
import ru.yandex.practicum.analyzer.dal.repository.ScenarioRepository;
import ru.yandex.practicum.analyzer.dal.service.SnapshotService;
import ru.yandex.practicum.analyzer.service.GrpcHubClient;
import ru.yandex.practicum.dto.scenario.ScenarioConditionType;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;
import java.util.Map;

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

        scenarios.stream()
                .filter(e -> scenarioFilter(e, snapshot.getSensorsState()))
                .map(scenarioActionRepository::findByScenario)
                .flatMap(List::stream)
                .forEach(hubClient::sendAction);
    }

    private boolean scenarioFilter(Scenario scenario, Map<String, SensorStateAvro> sensorsState) {
        List<ScenarioCondition> conditions = scenarioConditionRepository.findByScenario(scenario);

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
