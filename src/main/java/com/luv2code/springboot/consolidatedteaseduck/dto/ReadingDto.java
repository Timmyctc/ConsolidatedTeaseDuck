package com.luv2code.springboot.consolidatedteaseduck.dto;

import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ReadingDto(
        @NotNull MetricType metric,
        @NotNull @DecimalMin(value = "-100") @DecimalMax("300") double value,
        @NotNull Instant recordedAt) {}
