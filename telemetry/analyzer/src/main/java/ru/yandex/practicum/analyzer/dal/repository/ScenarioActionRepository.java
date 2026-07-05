package ru.yandex.practicum.analyzer.dal.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.dal.model.Scenario;
import ru.yandex.practicum.analyzer.dal.model.ScenarioAction;
import ru.yandex.practicum.analyzer.dal.model.ScenarioActionId;

import java.util.List;

public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, ScenarioActionId> {

    void deleteByScenario(Scenario scenario);

    List<ScenarioAction> findByScenario(Scenario scenario);

    @EntityGraph(attributePaths = {"scenario", "sensor", "action"})
    List<ScenarioAction> findByScenarioIn(List<Scenario> scenarios);

}
