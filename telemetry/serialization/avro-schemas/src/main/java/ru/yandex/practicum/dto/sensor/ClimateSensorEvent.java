package ru.yandex.practicum.dto.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Событие климатического датчика, содержащее информацию о температуре, влажности и уровне CO2.
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class ClimateSensorEvent extends SensorEvent {
    @NotNull
    private Integer temperatureC;
    @NotNull
    private Integer humidity;
    @NotNull
    private Integer co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
