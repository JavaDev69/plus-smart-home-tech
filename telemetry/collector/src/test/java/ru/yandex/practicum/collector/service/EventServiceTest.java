package ru.yandex.practicum.collector.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.collector.dto.DeviceType;
import ru.yandex.practicum.collector.dto.hub.DeviceAddedEvent;
import ru.yandex.practicum.collector.dto.hub.DeviceRemovedEvent;
import ru.yandex.practicum.collector.dto.hub.HubEvent;
import ru.yandex.practicum.collector.dto.hub.ScenarioAddedEvent;
import ru.yandex.practicum.collector.dto.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.collector.dto.sensor.ClimateSensorEvent;
import ru.yandex.practicum.collector.dto.sensor.LightSensorEvent;
import ru.yandex.practicum.collector.dto.sensor.MotionSensorEvent;
import ru.yandex.practicum.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.collector.dto.sensor.SwitchSensorEvent;
import ru.yandex.practicum.collector.dto.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.collector.kafka.KafkaAvroProducer;
import ru.yandex.practicum.collector.mapper.HubEventMapper;
import ru.yandex.practicum.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    private static final Instant TIMESTAMP = Instant.parse("2026-06-14T10:15:30Z");

    @Mock
    private KafkaAvroProducer producer;

    @Mock
    private SensorEventMapper sensorEventMapper;

    @Mock
    private HubEventMapper hubEventMapper;

    @InjectMocks
    private EventService eventService;

    @ParameterizedTest(name = "{0}")
    @MethodSource("sensorEventsDataProvider")
    @DisplayName("Should map and send sensor event")
    void collectSensorEvent_shouldCallSensorMapper(String testName, SensorEvent event) {
        SensorEventAvro avro = new SensorEventAvro();
        when(sensorEventMapper.toAvro(event)).thenReturn(avro);

        eventService.collectSensorEvent(event);

        verify(sensorEventMapper).toAvro(event);
        verify(producer).send(avro);
        verifyNoInteractions(hubEventMapper);
        verifyNoMoreInteractions(sensorEventMapper, producer);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("hubEventsDataProvider")
    @DisplayName("Should map and send hub event")
    void collectHubEvent_shouldCallHubMapper(String testName, HubEvent event) {
        HubEventAvro avro = new HubEventAvro();
        when(hubEventMapper.toAvro(event)).thenReturn(avro);

        eventService.collectHubEvent(event);

        verify(hubEventMapper).toAvro(event);
        verify(producer).send(avro);
        verifyNoInteractions(sensorEventMapper);
        verifyNoMoreInteractions(hubEventMapper, producer);
    }

    private static Stream<Arguments> sensorEventsDataProvider() {
        return Stream.of(
                Arguments.of(
                        "climate sensor event",
                        ClimateSensorEvent.builder()
                                .id("sensor-1")
                                .hubId("hub-1")
                                .timestamp(TIMESTAMP)
                                .temperatureC(21)
                                .humidity(45)
                                .co2Level(400)
                                .build()
                ),
                Arguments.of(
                        "light sensor event",
                        LightSensorEvent.builder()
                                .id("sensor-1")
                                .hubId("hub-1")
                                .timestamp(TIMESTAMP)
                                .linkQuality(90)
                                .luminosity(650)
                                .build()
                ),
                Arguments.of(
                        "motion sensor event",
                        MotionSensorEvent.builder()
                                .id("sensor-1")
                                .hubId("hub-1")
                                .timestamp(TIMESTAMP)
                                .linkQuality(88)
                                .motion(true)
                                .voltage(220)
                                .build()
                ),
                Arguments.of(
                        "switch sensor event",
                        SwitchSensorEvent.builder()
                                .id("sensor-1")
                                .hubId("hub-1")
                                .timestamp(TIMESTAMP)
                                .state(true)
                                .build()
                ),
                Arguments.of(
                        "temperature sensor event",
                        TemperatureSensorEvent.builder()
                                .id("sensor-1")
                                .hubId("hub-1")
                                .timestamp(TIMESTAMP)
                                .temperatureC(21)
                                .temperatureF(70)
                                .build()
                )
        );
    }

    private static Stream<Arguments> hubEventsDataProvider() {
        return Stream.of(
                Arguments.of(
                        "device added event",
                        DeviceAddedEvent.builder()
                                .hubId("hub-1")
                                .timestamp(TIMESTAMP)
                                .id("device-1")
                                .deviceType(DeviceType.MOTION_SENSOR)
                                .build()
                ),
                Arguments.of(
                        "device removed event",
                        DeviceRemovedEvent.builder()
                                .hubId("hub-1")
                                .timestamp(TIMESTAMP)
                                .id("device-1")
                                .build()
                ),
                Arguments.of(
                        "scenario added event",
                        ScenarioAddedEvent.builder()
                                .hubId("hub-1")
                                .timestamp(TIMESTAMP)
                                .name("arrival")
                                .conditions(List.of())
                                .actions(List.of())
                                .build()
                ),
                Arguments.of(
                        "scenario removed event",
                        ScenarioRemovedEvent.builder()
                                .hubId("hub-1")
                                .timestamp(TIMESTAMP)
                                .name("arrival")
                                .build()
                )
        );
    }
}
