package ru.yandex.practicum.collector.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.kafka.KafkaAvroProducer;
import ru.yandex.practicum.collector.mapper.HubEventMapper;
import ru.yandex.practicum.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;

@RequiredArgsConstructor
@Service
@Slf4j
public class EventService {
    private final KafkaAvroProducer producer;
    private final SensorEventMapper sensorEventMapper;
    private final HubEventMapper hubEventMapper;

    public void collectSensorEvent(@Valid SensorEvent event) {
        log.info("Sent sensor event to Kafka: {}", event);
        producer.send(sensorEventMapper.toAvro(event));
    }

    public void collectHubEvent(@Valid HubEvent event) {
        log.info("Sent hub event to Kafka: {}", event);
        producer.send(hubEventMapper.toAvro(event));
    }
}
