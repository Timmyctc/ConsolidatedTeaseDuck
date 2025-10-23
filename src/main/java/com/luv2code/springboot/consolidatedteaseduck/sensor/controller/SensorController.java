package com.luv2code.springboot.consolidatedteaseduck.sensor.controller;

import com.luv2code.springboot.consolidatedteaseduck.sensor.dto.CreateSensorRequest;
import com.luv2code.springboot.consolidatedteaseduck.sensor.dto.SensorResponse;
import com.luv2code.springboot.consolidatedteaseduck.sensor.entity.Sensor;
import com.luv2code.springboot.consolidatedteaseduck.sensor.service.SensorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public List<Sensor> getAllSensors() {
        return sensorService.getAllSensors();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public SensorResponse registerNewSensor(@Valid @RequestBody CreateSensorRequest sensor) {
        return sensorService.registerNewSensor(sensor);
    }
}
