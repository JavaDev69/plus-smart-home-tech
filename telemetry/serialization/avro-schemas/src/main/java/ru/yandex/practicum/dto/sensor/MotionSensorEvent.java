package ru.yandex.practicum.dto.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Событие датчика движения.
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class MotionSensorEvent extends SensorEvent {
    @NotNull
    private Integer linkQuality;
    @NotNull
    private Boolean motion;
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
