package ru.yandex.practicum.analyzer.dal.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.dal.model.Scenario;
import ru.yandex.practicum.analyzer.dal.model.ScenarioCondition;
import ru.yandex.practicum.analyzer.dal.model.ScenarioConditionId;

import java.util.List;

public interface ScenarioConditionRepository extends JpaRepository<ScenarioCondition, ScenarioConditionId> {
    void deleteByScenario(Scenario scenario);

    List<ScenarioCondition> findByScenario(Scenario scenario);

    @EntityGraph(attributePaths = {"scenario", "sensor", "condition"})
    List<ScenarioCondition> findByScenarioIn(List<Scenario> scenarios);
}