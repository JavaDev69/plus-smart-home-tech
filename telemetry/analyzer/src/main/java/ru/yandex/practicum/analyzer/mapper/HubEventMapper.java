package ru.yandex.practicum.analyzer.mapper;

import org.mapstruct.*;
import ru.yandex.practicum.dto.DeviceType;
import ru.yandex.practicum.dto.hub.DeviceAddedEvent;
import ru.yandex.practicum.dto.hub.DeviceRemovedEvent;
import ru.yandex.practicum.dto.hub.ScenarioAddedEvent;
import ru.yandex.practicum.dto.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.dto.scenario.DeviceAction;
import ru.yandex.practicum.dto.scenario.ScenarioCondition;
import ru.yandex.practicum.dto.scenario.ScenarioConditionType;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface HubEventMapper {

    @Mapping(target = "id", expression = "java(payloadToDeviceAdded(event.getPayload()).getId())")
    @Mapping(target = "deviceType", expression = "java(map(payloadToDeviceAdded(event.getPayload()).getType()))")
    DeviceAddedEvent mapToDeviceAdded(HubEventAvro event);

    @Mapping(target = "id", expression = "java(payloadToDeviceRemoved(event.getPayload()).getId())")
    DeviceRemovedEvent mapToDeviceRemoved(HubEventAvro event);

    @Mapping(target = "name", expression = "java(payloadToScenarioAdded(event.getPayload()).getName())")
    @Mapping(target = "conditions", expression = "java(mapCondition(payloadToScenarioAdded(event.getPayload()).getConditions()))")
    @Mapping(target = "actions", expression = "java(mapAction(payloadToScenarioAdded(event.getPayload()).getActions()))")
    ScenarioAddedEvent mapToScenarioAdded(HubEventAvro event);

    @Mapping(target = "name", expression = "java(payloadToScenarioRemoved(event.getPayload()).getName())")
    ScenarioRemovedEvent mapToScenarioRemoved(HubEventAvro event);

    @Named("mapScenarioCondition")
    ScenarioCondition map(ScenarioConditionAvro conditionAvro);

    @Named("mapDeviceAction")
    DeviceAction mapAction(DeviceActionAvro action);

    @IterableMapping(qualifiedByName = "mapDeviceAction")
    List<DeviceAction> mapAction(List<DeviceActionAvro> actions);

    @IterableMapping(qualifiedByName = "mapScenarioCondition")
    List<ScenarioCondition> mapCondition(List<ScenarioConditionAvro> conditions);

    ScenarioConditionType map(ConditionTypeAvro typeAvro);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.THROW_EXCEPTION)
    DeviceType map(DeviceTypeAvro deviceType);

    default DeviceAddedEventAvro payloadToDeviceAdded(Object payload) {
        return cast(payload, DeviceAddedEventAvro.class);
    }

    default DeviceRemovedEventAvro payloadToDeviceRemoved(Object payload) {
        return cast(payload, DeviceRemovedEventAvro.class);
    }

    default ScenarioAddedEventAvro payloadToScenarioAdded(Object payload) {
        return cast(payload, ScenarioAddedEventAvro.class);
    }

    default ScenarioRemovedEventAvro payloadToScenarioRemoved(Object payload) {
        return cast(payload, ScenarioRemovedEventAvro.class);
    }

    private static <T> T cast(Object value, Class<T> expectedType) {
        if (value == null) {
            return null;
        }

        if (!expectedType.isInstance(value)) {
            throw new IllegalArgumentException(
                    "Expected " + expectedType.getName() + ", but got: " + value.getClass().getName()
            );
        }

        return expectedType.cast(value);
    }
}
