package ru.yandex.practicum.collector.dto.scenario;

/**
 * Перечисление возможных типов действий при срабатывании условия активации сценария.
 */
public enum DeviceActionType {
    ACTIVATE,
    DEACTIVATE,
    INVERSE,
    SET_VALUE
}
