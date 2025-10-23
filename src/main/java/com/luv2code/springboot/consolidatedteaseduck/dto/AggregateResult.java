package com.luv2code.springboot.consolidatedteaseduck.dto;

import com.luv2code.springboot.consolidatedteaseduck.domain.AggregationType;
import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType;

import java.time.Instant;

public record AggregateResult(
        String sensorName,
        MetricType metricType,
        AggregationType aggregationType,
        Double value,
        Long count,
        Instant startTime,
        Instant endTime) {}
