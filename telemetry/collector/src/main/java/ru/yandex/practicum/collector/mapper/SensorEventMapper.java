package ru.yandex.practicum.collector.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.dto.sensor.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface SensorEventMapper {
    default SensorEventAvro toAvro(SensorEvent event) {
        if (event == null) {
            return null;
        }

        SensorEventAvro avro = new SensorEventAvro();
        avro.setId(event.getId());
        avro.setHubId(event.getHubId());
        avro.setTimestamp(event.getTimestamp());
        avro.setPayload(toPayload(event));
        return avro;
    }

    default Object toPayload(SensorEvent event) {
        if (event instanceof ClimateSensorEvent climateSensorEvent) {
            return toPayload(climateSensorEvent);
        }
        if (event instanceof LightSensorEvent lightSensorEvent) {
            return toPayload(lightSensorEvent);
        }
        if (event instanceof MotionSensorEvent motionSensorEvent) {
            return toPayload(motionSensorEvent);
        }
        if (event instanceof SwitchSensorEvent switchSensorEvent) {
            return toPayload(switchSensorEvent);
        }
        if (event instanceof TemperatureSensorEvent temperatureSensorEvent) {
            return toPayload(temperatureSensorEvent);
        }

        throw new IllegalArgumentException("Unsupported sensor event type: " + event.getClass());
    }

    ClimateSensorAvro toPayload(ClimateSensorEvent event);

    LightSensorAvro toPayload(LightSensorEvent event);

    MotionSensorAvro toPayload(MotionSensorEvent event);

    SwitchSensorAvro toPayload(SwitchSensorEvent event);

    TemperatureSensorAvro toPayload(TemperatureSensorEvent event);
}
