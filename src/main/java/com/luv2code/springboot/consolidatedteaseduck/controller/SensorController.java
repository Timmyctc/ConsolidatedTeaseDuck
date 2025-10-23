package com.luv2code.springboot.consolidatedteaseduck.controller;

import com.luv2code.springboot.consolidatedteaseduck.service.SensorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;


    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }
}
