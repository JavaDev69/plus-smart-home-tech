package ru.yandex.practicum.analyzer.mapper;

import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ValueMapping;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.dal.model.ScenarioAction;
import ru.yandex.practicum.dto.scenario.DeviceActionType;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;

import java.time.Instant;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceActionMapper {
    @Mapping(target = "scenarioName", source = "scenario.name")
    @Mapping(target = "hubId", source = "sensor.hubId")
    @Mapping(target = "action", source = "scenarioAction", qualifiedByName = "mapAction")
    @Mapping(target = "timestamp", expression = "java(getCurrentTimestamp())")
    DeviceActionRequest map(ScenarioAction scenarioAction);

    @Named("mapAction")
    default DeviceActionProto mapAction(ScenarioAction scenarioAction) {
        Integer value = scenarioAction.getAction().getValue();
        DeviceActionProto.Builder builder = DeviceActionProto.newBuilder()
                .setType(map(scenarioAction.getAction().getType()))
                .setSensorId(scenarioAction.getSensor().getId());
        if (value != null) {
            builder.setValue(value);
        }
        return builder.build();
    }

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.THROW_EXCEPTION)
    ActionTypeProto map(DeviceActionType type);

    default Timestamp getCurrentTimestamp() {
        Instant now = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();
    }
}
