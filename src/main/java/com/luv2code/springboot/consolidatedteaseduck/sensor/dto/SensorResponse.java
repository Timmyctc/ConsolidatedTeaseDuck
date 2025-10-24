package com.luv2code.springboot.consolidatedteaseduck.sensor.dto;

import java.time.Instant;

/**
 * DTO for Sensor that returns information on the sensor
 * @param name Sensor unique name
 * @param location Location of the Sensor
 * @param createdAt Instant the Sensor was registered
 */
public record SensorResponse(
        String name,
        String location,
        Instant createdAt
){}
