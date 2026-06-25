package ru.yandex.practicum.collector.handler;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Log
@Component
public class UnknownEventHandler implements SensorEventHandler, HubEventHandler {
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.PAYLOAD_NOT_SET;
    }

    @Override
    public void handle(SensorEventProto event) {
        log.warning("Unknown event received: " + event);
        throw new IllegalStateException("Отсутствует обработчик для события " + event.getPayloadCase());
    }

    @Override
    public HubEventProto.PayloadCase getEventType() {
        return HubEventProto.PayloadCase.PAYLOAD_NOT_SET;
    }

    @Override
    public void handle(HubEventProto event) {
        log.warning("Unknown event received: " + event);
        throw new IllegalStateException("Отсутствует обработчик для события " + event.getPayloadCase());
    }
}
