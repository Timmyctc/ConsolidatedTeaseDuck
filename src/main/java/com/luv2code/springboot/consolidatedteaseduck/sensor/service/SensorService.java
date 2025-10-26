package com.luv2code.springboot.consolidatedteaseduck.sensor.service;

import com.luv2code.springboot.consolidatedteaseduck.sensor.dto.CreateSensorRequest;
import com.luv2code.springboot.consolidatedteaseduck.sensor.dto.SensorResponse;
import com.luv2code.springboot.consolidatedteaseduck.sensor.entity.Sensor;
import com.luv2code.springboot.consolidatedteaseduck.exception.DuplicateSensorNameException;
import com.luv2code.springboot.consolidatedteaseduck.sensor.repository.SensorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public ResponseEntity<Sensor> getSensorByName(String name) {
        Optional<Sensor> sensor = repository.findByNameIgnoreCase(name);
        if(sensor.isPresent()) return ResponseEntity.ok(sensor.get());
        else return ResponseEntity.notFound().build();
    }

    @Transactional
    public Sensor registerNewSensor(final CreateSensorRequest createSensorRequest) {
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
