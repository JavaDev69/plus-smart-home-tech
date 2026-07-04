package ru.yandex.practicum.dto.scenario;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Операции, которые могут быть использованы в условиях.
 */
public enum ScenarioConditionOperation implements BiPredicate<Integer, Integer> {
    EQUALS {
        @Override
        public boolean test(Integer a, Integer b) {
            return Objects.equals(a, b);
        }
    },
    GREATER_THAN {
        @Override
        public boolean test(Integer a, Integer b) {
            return a > b;
        }
    },
    LOWER_THAN {
        @Override
        public boolean test(Integer a, Integer b) {
            return a < b;
        }
    };

    abstract public boolean test(Integer a, Integer b);
}
