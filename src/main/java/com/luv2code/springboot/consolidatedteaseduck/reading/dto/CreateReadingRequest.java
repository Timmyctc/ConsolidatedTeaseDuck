package com.luv2code.springboot.consolidatedteaseduck.reading.dto;

import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * A record that holds A request to create a sensor reading
 *
 * @param sensorName Name to uniquely identify the Sensor
 * @param metricType The MetricType of the reading (Temp,Humidity etc)
 * @param value The value of the reading
 * @param timestamp The time the reading was recorded at
 */
public record CreateReadingRequest(
        @NotBlank String sensorName,
        @NotNull MetricType metricType,
        @NotNull Double value,
        Instant timestamp
) {}
