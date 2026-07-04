package ru.yandex.practicum.collector.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.collector.service.EventService;
import ru.yandex.practicum.dto.hub.*;
import ru.yandex.practicum.dto.sensor.*;

import java.time.Instant;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {
    private static final Instant TIMESTAMP = Instant.parse("2026-06-14T10:15:30Z");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @ParameterizedTest(name = "{0}")
    @MethodSource("validSensorEventsDataProvider")
    @DisplayName("Should deserialize all valid sensor event parameters")
    void collectSensorEvent_whenRequestIsValid_shouldDeserializeEvent(
            String testName,
            String json,
            Class<? extends SensorEvent> eventClass
    ) throws Exception {
        mockMvc.perform(post("/events/sensors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        ArgumentCaptor<SensorEvent> eventCaptor = ArgumentCaptor.forClass(SensorEvent.class);
        verify(eventService).collectSensorEvent(eventCaptor.capture());
        verifyNoMoreInteractions(eventService);

        SensorEvent event = eventCaptor.getValue();
        assertThat(event).isInstanceOf(eventClass);
        assertThat(event.getId()).isEqualTo("sensor-1");
        assertThat(event.getHubId()).isEqualTo("hub-1");
        assertThat(event.getTimestamp()).isEqualTo(TIMESTAMP);
        assertSensorPayload(event);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("validHubEventsDataProvider")
    @DisplayName("Should deserialize all valid hub event parameters")
    void collectHubEvent_whenRequestIsValid_shouldDeserializeEvent(
            String testName,
            String json,
            Class<? extends HubEvent> eventClass
    ) throws Exception {
        mockMvc.perform(post("/events/hubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        ArgumentCaptor<HubEvent> eventCaptor = ArgumentCaptor.forClass(HubEvent.class);
        verify(eventService).collectHubEvent(eventCaptor.capture());
        verifyNoMoreInteractions(eventService);

        HubEvent event = eventCaptor.getValue();
        assertThat(event).isInstanceOf(eventClass);
        assertThat(event.getHubId()).isEqualTo("hub-1");
        assertThat(event.getTimestamp()).isEqualTo(TIMESTAMP);
        assertHubPayload(event);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidEventsDataProvider")
    @DisplayName("Should return bad request for invalid event parameters")
    void collectEvent_whenRequestIsInvalid_shouldReturnBadRequest(
            String testName,
            String path,
            String json
    ) throws Exception {
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(eventService);
    }

    private static Stream<Arguments> validSensorEventsDataProvider() {
        return Stream.of(
                Arguments.of(
                        "climate sensor event",
                        """
                                {
                                  "id": "sensor-1",
                                  "hubId": "hub-1",
                                  "timestamp": "2026-06-14T10:15:30Z",
                                  "type": "CLIMATE_SENSOR_EVENT",
                                  "temperatureC": 21,
                                  "humidity": 45,
                                  "co2Level": 400
                                }
                                """,
                        ClimateSensorEvent.class
                ),
                Arguments.of(
                        "light sensor event",
                        """
                                {
                                  "id": "sensor-1",
                                  "hubId": "hub-1",
                                  "timestamp": "2026-06-14T10:15:30Z",
                                  "type": "LIGHT_SENSOR_EVENT",
                                  "linkQuality": 90,
                                  "luminosity": 650
                                }
                                """,
                        LightSensorEvent.class
                ),
                Arguments.of(
                        "motion sensor event",
                        """
                                {
                                  "id": "sensor-1",
                                  "hubId": "hub-1",
                                  "timestamp": "2026-06-14T10:15:30Z",
                                  "type": "MOTION_SENSOR_EVENT",
                                  "linkQuality": 88,
                                  "motion": true,
                                  "voltage": 220
                                }
                                """,
                        MotionSensorEvent.class
                ),
                Arguments.of(
                        "switch sensor event",
                        """
                                {
                                  "id": "sensor-1",
                                  "hubId": "hub-1",
                                  "timestamp": "2026-06-14T10:15:30Z",
                                  "type": "SWITCH_SENSOR_EVENT",
                                  "state": true
                                }
                                """,
                        SwitchSensorEvent.class
                ),
                Arguments.of(
                        "temperature sensor event",
                        """
                                {
                                  "id": "sensor-1",
                                  "hubId": "hub-1",
                                  "timestamp": "2026-06-14T10:15:30Z",
                                  "type": "TEMPERATURE_SENSOR_EVENT",
                                  "temperatureC": 21,
                                  "temperatureF": 70
                                }
                                """,
                        TemperatureSensorEvent.class
                )
        );
    }

    private static Stream<Arguments> validHubEventsDataProvider() {
        return Stream.of(
                Arguments.of(
                        "device added event",
                        """
                                {
                                  "hubId": "hub-1",
                                  "timestamp": "2026-06-14T10:15:30Z",
                                  "type": "DEVICE_ADDED",
                                  "id": "device-1",
                                  "deviceType": "MOTION_SENSOR"
                                }
                                """,
                        DeviceAddedEvent.class
                ),
                Arguments.of(
                        "device removed event",
                        """
                                {
                                  "hubId": "hub-1",
                                  "timestamp": "2026-06-14T10:15:30Z",
                                  "type": "DEVICE_REMOVED",
                                  "id": "device-1"
                                }
                                """,
                        DeviceRemovedEvent.class
                ),
                Arguments.of(
                        "scenario added event",
                        """
                                {
                                  "hubId": "hub-1",
                                  "timestamp": "2026-06-14T10:15:30Z",
                                  "type": "SCENARIO_ADDED",
                                  "name": "arrival",
                                  "conditions": [
                                    {
                                      "sensorId": "sensor-1",
                                      "type": "MOTION",
                                      "operation": "EQUALS",
                                      "value": 1
                                    }
                                  ],
                                  "actions": [
                                    {
                                      "sensorId": "sensor-2",
                                      "type": "ACTIVATE",
                                      "value": 1
                                    }
                                  ]
                                }
                                """,
                        ScenarioAddedEvent.class
                ),
                Arguments.of(
                        "scenario removed event",
                        """
                                {
                                  "hubId": "hub-1",
                                  "timestamp": "2026-06-14T10:15:30Z",
                                  "type": "SCENARIO_REMOVED",
                                  "name": "arrival"
                                }
                                """,
                        ScenarioRemovedEvent.class
                )
        );
    }

    private static Stream<Arguments> invalidEventsDataProvider() {
        return Stream.of(
                Arguments.of(
                        "sensor id is required",
                        "/events/sensors",
                        """
                                {
                                  "hubId": "hub-1",
                                  "type": "SWITCH_SENSOR_EVENT",
                                  "state": true
                                }
                                """
                ),
                Arguments.of(
                        "sensor hub id should not be blank",
                        "/events/sensors",
                        """
                                {
                                  "id": "sensor-1",
                                  "hubId": " ",
                                  "type": "SWITCH_SENSOR_EVENT",
                                  "state": true
                                }
                                """
                ),
                Arguments.of(
                        "switch sensor state is required",
                        "/events/sensors",
                        """
                                {
                                  "id": "sensor-1",
                                  "hubId": "hub-1",
                                  "type": "SWITCH_SENSOR_EVENT"
                                }
                                """
                ),
                Arguments.of(
                        "unknown sensor event type",
                        "/events/sensors",
                        """
                                {
                                  "id": "sensor-1",
                                  "hubId": "hub-1",
                                  "type": "UNKNOWN_SENSOR_EVENT"
                                }
                                """
                ),
                Arguments.of(
                        "hub id is required",
                        "/events/hubs",
                        """
                                {
                                  "type": "DEVICE_REMOVED",
                                  "id": "device-1"
                                }
                                """
                ),
                Arguments.of(
                        "device type is required",
                        "/events/hubs",
                        """
                                {
                                  "hubId": "hub-1",
                                  "type": "DEVICE_ADDED",
                                  "id": "device-1"
                                }
                                """
                ),
                Arguments.of(
                        "scenario conditions should not be empty",
                        "/events/hubs",
                        """
                                {
                                  "hubId": "hub-1",
                                  "type": "SCENARIO_ADDED",
                                  "name": "arrival",
                                  "conditions": [],
                                  "actions": [
                                    {
                                      "sensorId": "sensor-2",
                                      "type": "ACTIVATE",
                                      "value": 1
                                    }
                                  ]
                                }
                                """
                ),
                Arguments.of(
                        "scenario name is too short",
                        "/events/hubs",
                        """
                                {
                                  "hubId": "hub-1",
                                  "type": "SCENARIO_REMOVED",
                                  "name": "go"
                                }
                                """
                )
        );
    }

    private static void assertSensorPayload(SensorEvent event) {
        if (event instanceof ClimateSensorEvent climateSensorEvent) {
            assertThat(climateSensorEvent.getTemperatureC()).isEqualTo(21);
            assertThat(climateSensorEvent.getHumidity()).isEqualTo(45);
            assertThat(climateSensorEvent.getCo2Level()).isEqualTo(400);
        } else if (event instanceof LightSensorEvent lightSensorEvent) {
            assertThat(lightSensorEvent.getLinkQuality()).isEqualTo(90);
            assertThat(lightSensorEvent.getLuminosity()).isEqualTo(650);
        } else if (event instanceof MotionSensorEvent motionSensorEvent) {
            assertThat(motionSensorEvent.getLinkQuality()).isEqualTo(88);
            assertThat(motionSensorEvent.getMotion()).isTrue();
            assertThat(motionSensorEvent.getVoltage()).isEqualTo(220);
        } else if (event instanceof SwitchSensorEvent switchSensorEvent) {
            assertThat(switchSensorEvent.getState()).isTrue();
        } else if (event instanceof TemperatureSensorEvent temperatureSensorEvent) {
            assertThat(temperatureSensorEvent.getTemperatureC()).isEqualTo(21);
            assertThat(temperatureSensorEvent.getTemperatureF()).isEqualTo(70);
        }
    }

    private static void assertHubPayload(HubEvent event) {
        if (event instanceof DeviceAddedEvent deviceAddedEvent) {
            assertThat(deviceAddedEvent.getId()).isEqualTo("device-1");
            assertThat(deviceAddedEvent.getDeviceType().name()).isEqualTo("MOTION_SENSOR");
        } else if (event instanceof DeviceRemovedEvent deviceRemovedEvent) {
            assertThat(deviceRemovedEvent.getId()).isEqualTo("device-1");
        } else if (event instanceof ScenarioAddedEvent scenarioAddedEvent) {
            assertThat(scenarioAddedEvent.getName()).isEqualTo("arrival");
            assertThat(scenarioAddedEvent.getConditions()).hasSize(1);
            assertThat(scenarioAddedEvent.getConditions().getFirst().getSensorId()).isEqualTo("sensor-1");
            assertThat(scenarioAddedEvent.getConditions().getFirst().getType().name()).isEqualTo("MOTION");
            assertThat(scenarioAddedEvent.getConditions().getFirst().getOperation().name()).isEqualTo("EQUALS");
            assertThat(scenarioAddedEvent.getConditions().getFirst().getValue()).isEqualTo(1);
            assertThat(scenarioAddedEvent.getActions()).hasSize(1);
            assertThat(scenarioAddedEvent.getActions().getFirst().getSensorId()).isEqualTo("sensor-2");
            assertThat(scenarioAddedEvent.getActions().getFirst().getType().name()).isEqualTo("ACTIVATE");
            assertThat(scenarioAddedEvent.getActions().getFirst().getValue()).isEqualTo(1);
        } else if (event instanceof ScenarioRemovedEvent scenarioRemovedEvent) {
            assertThat(scenarioRemovedEvent.getName()).isEqualTo("arrival");
        }
    }
}
