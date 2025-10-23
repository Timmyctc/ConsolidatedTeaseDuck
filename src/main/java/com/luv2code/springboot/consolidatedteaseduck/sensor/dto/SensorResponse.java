package com.luv2code.springboot.consolidatedteaseduck.sensor.dto;

import java.time.Instant;

public record SensorResponse(
        Long id,
        String name,
        String location,
        Instant createdAt
){}
