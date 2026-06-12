package ru.yandex.practicum.collector.dto.scenario;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Представляет действие, которое должно быть выполнено устройством.
 */
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class DeviceAction {
    private String sensorId;
    private DeviceActionType type;
    private Integer value;
}
