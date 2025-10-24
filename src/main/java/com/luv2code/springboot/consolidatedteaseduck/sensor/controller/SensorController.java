package com.luv2code.springboot.consolidatedteaseduck.sensor.controller;

import com.luv2code.springboot.consolidatedteaseduck.sensor.dto.CreateSensorRequest;
import com.luv2code.springboot.consolidatedteaseduck.sensor.dto.SensorResponse;
import com.luv2code.springboot.consolidatedteaseduck.sensor.entity.Sensor;
import com.luv2code.springboot.consolidatedteaseduck.sensor.service.SensorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest Controller for Sensors: GET all, GET by name and POST new Sensors
 */
@RestController
@RequestMapping("/api/v1/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    /**
     * GET all Sensors
     * @return List of SensorResponse DTOs
     */
    @GetMapping
    public List<SensorResponse> getAllSensors() {
        return sensorService.getAllSensors()
                .stream()
                .map(this::convertToSensorResponse)
                .collect(Collectors.toList());
    }

    /**
     * POST new sensor
     * @param sensor CreateSensorRequest object encapsulates just the necessary info for registering new sensor
     * @return Returns the SensorResponse object for new Sensor that has been registered
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public SensorResponse registerNewSensor(@Valid @RequestBody CreateSensorRequest sensor) {
        return convertToSensorResponse(sensorService.registerNewSensor(sensor));
    }

    public SensorResponse convertToSensorResponse(final Sensor sensor){
        return new SensorResponse(
                sensor.getName(),
                sensor.getLocation(),
                sensor.getCreatedAt()
        );
    }
}
