package ru.yandex.practicum.analyzer.dal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
