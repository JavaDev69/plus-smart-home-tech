package ru.yandex.practicum.analyzer.dal.service;

import ru.yandex.practicum.analyzer.dal.model.Sensor;

import java.util.Collection;
import java.util.Optional;

public interface SensorService {
    void save(Sensor sensor);

    void delete(Sensor sensor);

    Collection<Sensor> getSensors();

    Collection<Sensor> getSensorByHubId(String hubId);

    Optional<Sensor> getSensorByIdAndHubId(String id, String hubId);
}
 