package ru.yandex.practicum.dto.scenario;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.dto.HasSensorId;

/**
 * Условие сценария, которое содержит информацию о датчике, типе условия, операции и значении.
 */
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class ScenarioCondition implements HasSensorId {
    private String sensorId;
    private ScenarioConditionType type;
    private ScenarioConditionOperation operation;
    private Integer value;
}
