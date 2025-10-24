package com.luv2code.springboot.consolidatedteaseduck.sensor.service;

import com.luv2code.springboot.consolidatedteaseduck.sensor.dto.CreateSensorRequest;
import com.luv2code.springboot.consolidatedteaseduck.sensor.dto.SensorResponse;
import com.luv2code.springboot.consolidatedteaseduck.sensor.entity.Sensor;
import com.luv2code.springboot.consolidatedteaseduck.exception.DuplicateSensorNameException;
import com.luv2code.springboot.consolidatedteaseduck.sensor.repository.SensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for Sensor
 */
@Service
public class SensorService {

    private SensorRepository repository;

    public SensorService(SensorRepository repository) {
        this.repository=repository;
    }

    @Transactional(readOnly = true)
    public List<Sensor> getAllSensors() {
        return repository.findAll();
    }

    @Transactional
    public Sensor registerNewSensor(CreateSensorRequest createSensorRequest) {
        if (repository.existsByNameIgnoreCase(createSensorRequest.name())) {
            throw new DuplicateSensorNameException(createSensorRequest.name());
        } else {
            Sensor sensorToAdd = new Sensor();
            sensorToAdd.setName(createSensorRequest.name());
            sensorToAdd.setLocation(createSensorRequest.location());
            Sensor savedSensor = repository.save(sensorToAdd);
            return savedSensor;
        }
    }

}
