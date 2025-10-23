package com.luv2code.springboot.consolidatedteaseduck.reading.dto;

import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateReadingRequest(
        @NotBlank String sensorName,
        @NotNull MetricType metricType,
        @NotNull Double value,
        Instant timestamp
) {}
