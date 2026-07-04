package ru.yandex.practicum.analyzer.dal.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "scenario_actions")
public class ScenarioAction {
    @EmbeddedId
    private ScenarioActionId id;

    @MapsId("scenarioId")
    @JoinColumn(name = "scenario_id")
    @ManyToOne
    private Scenario scenario;

    @MapsId("sensorId")
    @JoinColumn(name = "sensor_id")
    @ManyToOne
    private Sensor sensor;

    @MapsId("actionId")
    @JoinColumn(name = "action_id")
    @ManyToOne
    private Action action;
}
