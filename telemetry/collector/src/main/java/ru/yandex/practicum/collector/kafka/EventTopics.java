package ru.yandex.practicum.collector.kafka;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventTopics {
    public static final String HUB_TOPIC_V1 = "telemetry.hubs.v1";
    public static final String SENSOR_TOPIC_V1 = "telemetry.sensors.v1";
}
