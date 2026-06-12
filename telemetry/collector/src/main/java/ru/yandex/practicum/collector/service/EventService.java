package ru.yandex.practicum.collector.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.collector.kafka.KafkaAvroProducer;
import ru.yandex.practicum.collector.mapper.HubEventMapper;
import ru.yandex.practicum.collector.mapper.SensorEventMapper;

@RequiredArgsConstructor
@Service
public class EventService {
    private final KafkaAvroProducer producer;
    private final SensorEventMapper sensorEventMapper;
    private final HubEventMapper hubEventMapper;

    public void collectSensorEvent(@Valid SensorEvent event) {
        producer.send(sensorEventMapper.toAvro(event));
    }

    public void collectHubEvent(@Valid HubEvent event) {
        producer.send(hubEventMapper.toAvro(event));
    }
}
