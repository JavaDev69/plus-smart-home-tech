package ru.yandex.practicum.analyzer.dal.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.dto.scenario.ScenarioConditionOperation;
import ru.yandex.practicum.dto.scenario.ScenarioConditionType;

@Getter
@Setter
@Builder
@Entity
@Table(name = "conditions")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ScenarioConditionType type;

    @Enumerated(EnumType.STRING)
    private ScenarioConditionOperation operation;

    private Integer value;
}
