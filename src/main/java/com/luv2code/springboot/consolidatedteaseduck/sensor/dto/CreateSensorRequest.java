package com.luv2code.springboot.consolidatedteaseduck.sensor.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSensorRequest(
        @NotBlank String name,
        String location
) {}
