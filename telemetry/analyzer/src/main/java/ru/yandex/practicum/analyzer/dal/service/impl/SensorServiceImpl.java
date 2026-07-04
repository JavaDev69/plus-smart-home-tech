package ru.yandex.practicum.analyzer.dal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.dal.model.Sensor;
import ru.yandex.practicum.analyzer.dal.repository.SensorRepository;
import ru.yandex.practicum.analyzer.dal.service.SensorService;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SensorServiceImpl implements SensorService {
    private final SensorRepository sensorRepository;

    @Override
    public void save(Sensor sensor) {
        log.debug("Saving sensor: {}", sensor);
        sensorRepository.save(sensor);
        log.debug("Saved sensor: {}", sensor);
    }

    @Override
    public void delete(Sensor sensor) {
        log.debug("Deleting sensor: {}", sensor);
        sensorRepository.delete(sensor);
        log.debug("Deleted sensor: {}", sensor);
    }

    @Override
    public Collection<Sensor> getSensors() {
        return sensorRepository.findAll();
    }

    @Override
    public Collection<Sensor> getSensorByHubId(String hubId) {
        return sensorRepository.findAllByHubId(hubId);
    }

    @Override
    public Optional<Sensor> getSensorByIdAndHubId(String id, String hubId) {
        return sensorRepository.findByIdAndHubId(id, hubId);
    }
}
