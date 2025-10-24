package com.luv2code.springboot.consolidatedteaseduck.sensor.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * A record that holds information to create a sensor
 *
 * @param name
 * @param location
 */
public record CreateSensorRequest(
        @NotBlank String name,
        @NotBlank String location
) {}
