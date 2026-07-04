package ru.yandex.practicum.analyzer.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.analyzer.dal.model.Condition;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
