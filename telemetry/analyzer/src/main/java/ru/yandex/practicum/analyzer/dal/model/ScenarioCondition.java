package ru.yandex.practicum.analyzer.dal.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "scenario_conditions")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class ScenarioCondition {

    @EmbeddedId
    @ToString.Include
    private ScenarioConditionId id;

    @MapsId("scenarioId")
    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @MapsId("sensorId")
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @MapsId("conditionId")
    @ManyToOne
    @JoinColumn(name = "condition_id")
    @ToString.Include
    private Condition condition;

}
