package ru.yandex.practicum.collector.mapper;

import org.mapstruct.Builder;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ValueMapping;
import ru.yandex.practicum.dto.DeviceType;
import ru.yandex.practicum.dto.hub.DeviceAddedEvent;
import ru.yandex.practicum.dto.hub.DeviceRemovedEvent;
import ru.yandex.practicum.dto.hub.ScenarioAddedEvent;
import ru.yandex.practicum.dto.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.dto.scenario.DeviceAction;
import ru.yandex.practicum.dto.scenario.DeviceActionType;
import ru.yandex.practicum.dto.scenario.ScenarioCondition;
import ru.yandex.practicum.dto.scenario.ScenarioConditionOperation;
import ru.yandex.practicum.dto.scenario.ScenarioConditionType;
import ru.yandex.practicum.dto.sensor.ClimateSensorEvent;
import ru.yandex.practicum.dto.sensor.LightSensorEvent;
import ru.yandex.practicum.dto.sensor.MotionSensorEvent;
import ru.yandex.practicum.dto.sensor.SwitchSensorEvent;
import ru.yandex.practicum.dto.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.ConditionOperationProto;
import ru.yandex.practicum.grpc.telemetry.event.ConditionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {
                TimestampsMapper.class
        }
)
public interface EventProtoMapper {
    @Mapping(target = "state", expression = "java(event.getSwitchSensor().getState())")
    SwitchSensorEvent mapToSwitchEvent(SensorEventProto event);

    @Mapping(target = "temperatureC", expression = "java(event.getClimateSensor().getTemperatureC())")
    @Mapping(target = "humidity", expression = "java(event.getClimateSensor().getHumidity())")
    @Mapping(target = "co2Level", expression = "java(event.getClimateSensor().getCo2Level())")
    ClimateSensorEvent mapToClimateEvent(SensorEventProto event);

    @Mapping(target = "linkQuality", expression = "java(event.getLightSensor().getLinkQuality())")
    @Mapping(target = "luminosity", expression = "java(event.getLightSensor().getLuminosity())")
    LightSensorEvent mapToLightEvent(SensorEventProto event);

    @Mapping(target = "linkQuality", expression = "java(event.getMotionSensor().getLinkQuality())")
    @Mapping(target = "motion", expression = "java(event.getMotionSensor().getMotion())")
    @Mapping(target = "voltage", expression = "java(event.getMotionSensor().getVoltage())")
    MotionSensorEvent mapToMotionEvent(SensorEventProto event);

    @Mapping(target = "temperatureC", expression = "java(event.getTemperatureSensor().getTemperatureC())")
    @Mapping(target = "temperatureF", expression = "java(event.getTemperatureSensor().getTemperatureF())")
    TemperatureSensorEvent mapToTemperatureEvent(SensorEventProto event);

    @Mapping(target = "id", expression = "java(event.getDeviceAdded().getId())")
    @Mapping(target = "deviceType", expression = "java(map(event.getDeviceAdded().getType()))")
    DeviceAddedEvent mapToDeviceAdded(HubEventProto event);

    @Mapping(target = "id", expression = "java(event.getDeviceRemoved().getId())")
    DeviceRemovedEvent mapToDeviceRemoved(HubEventProto event);

    @Mapping(target = "name", expression = "java(event.getScenarioAdded().getName())")
    @Mapping(target = "conditions", expression = "java(mapScenario(event.getScenarioAdded().getConditionList()))")
    @Mapping(target = "actions", expression = "java(mapAction(event.getScenarioAdded().getActionList()))")
    ScenarioAddedEvent mapToScenarioAdded(HubEventProto event);

    @Mapping(target = "name", expression = "java(event.getScenarioRemoved().getName())")
    ScenarioRemovedEvent mapToScenarioRemoved(HubEventProto event);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.THROW_EXCEPTION)
    DeviceType map(DeviceTypeProto type);

    @Named("mapScenarioCondition")
    @Mapping(target = "value", source = ".", qualifiedByName = "mapValue")
    ScenarioCondition mapScenario(ScenarioConditionProto condition);

    @IterableMapping(qualifiedByName = "mapScenarioCondition")
    List<ScenarioCondition> mapScenario(List<ScenarioConditionProto> conditions);

    @Named("mapDeviceAction")
    DeviceAction mapAction(DeviceActionProto action);

    @IterableMapping(qualifiedByName = "mapDeviceAction")
    List<DeviceAction> mapAction(List<DeviceActionProto> actions);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.THROW_EXCEPTION)
    DeviceActionType map(ActionTypeProto actionType);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.THROW_EXCEPTION)
    ScenarioConditionType map(ConditionTypeProto conditionType);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.THROW_EXCEPTION)
    ScenarioConditionOperation map(ConditionOperationProto conditionOperation);

    @Named("mapValue")
    default Integer getValue(ScenarioConditionProto condition) {
        if (condition == null) {
            return null;
        }
        ScenarioConditionProto.ValueCase valueCase = condition.getValueCase();
        return switch (valueCase) {
            case INT_VALUE -> condition.getIntValue();
            case BOOL_VALUE -> condition.getBoolValue() ? 1 : 0;
            default -> null;
        };
    }
}
