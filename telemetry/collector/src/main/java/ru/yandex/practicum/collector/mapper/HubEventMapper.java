package ru.yandex.practicum.collector.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.dto.hub.DeviceAddedEvent;
import ru.yandex.practicum.collector.dto.hub.DeviceRemovedEvent;
import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.hub.ScenarioAddedEvent;
import ru.yandex.practicum.collector.dto.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.collector.dto.scenario.DeviceAction;
import ru.yandex.practicum.collector.dto.scenario.ScenarioCondition;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
@Component
public interface HubEventMapper {
    default HubEventAvro toAvro(HubEvent event) {
        if (event == null) {
            return null;
        }

        HubEventAvro avro = new HubEventAvro();
        avro.setHubId(event.getHubId());
        avro.setTimestamp(event.getTimestamp());
        avro.setPayload(toPayload(event));
        return avro;
    }

    default Object toPayload(HubEvent event) {
        if (event instanceof DeviceAddedEvent deviceAddedEvent) {
            return toPayload(deviceAddedEvent);
        }
        if (event instanceof DeviceRemovedEvent deviceRemovedEvent) {
            return toPayload(deviceRemovedEvent);
        }
        if (event instanceof ScenarioAddedEvent scenarioAddedEvent) {
            return toPayload(scenarioAddedEvent);
        }
        if (event instanceof ScenarioRemovedEvent scenarioRemovedEvent) {
            return toPayload(scenarioRemovedEvent);
        }

        throw new IllegalArgumentException("Unsupported hub event type: " + event.getClass());
    }

    @Mapping(target = "type", source = "deviceType")
    DeviceAddedEventAvro toPayload(DeviceAddedEvent event);

    DeviceRemovedEventAvro toPayload(DeviceRemovedEvent event);

    ScenarioAddedEventAvro toPayload(ScenarioAddedEvent event);

    ScenarioRemovedEventAvro toPayload(ScenarioRemovedEvent event);

    ScenarioConditionAvro toAvro(ScenarioCondition condition);

    DeviceActionAvro toAvro(DeviceAction action);

}
