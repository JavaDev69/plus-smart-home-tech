package ru.yandex.practicum.dto.scenario;

import lombok.*;
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
