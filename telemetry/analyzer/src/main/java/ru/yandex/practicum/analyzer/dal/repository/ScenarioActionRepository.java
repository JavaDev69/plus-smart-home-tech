package ru.yandex.practicum.analyzer.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.analyzer.dal.model.Scenario;
import ru.yandex.practicum.analyzer.dal.model.ScenarioAction;
import ru.yandex.practicum.analyzer.dal.model.ScenarioActionId;

import java.util.List;

@Repository
public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, ScenarioActionId> {

    void deleteByScenario(Scenario scenario);

    List<ScenarioAction> findByScenario(Scenario scenario);

}
