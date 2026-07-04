package ru.yandex.practicum.dto.sensor;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Событие датчика освещенности, содержащее информацию о качестве связи и уровне освещенности.
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class LightSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Integer luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
