package ru.yandex.practicum.dto.scenario;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.dto.HasSensorId;

/**
 * Представляет действие, которое должно быть выполнено устройством.
 */
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class DeviceAction implements HasSensorId {
    private String sensorId;
    private DeviceActionType type;
    private Integer value;
}
