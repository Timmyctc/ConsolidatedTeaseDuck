package com.luv2code.springboot.consolidatedteaseduck.reading.dto;

import com.luv2code.springboot.consolidatedteaseduck.domain.AggregationType;
import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * The record that holds a client's request for reading aggregate data
 *
 * @param sensorNames List of names of the Sensors to retrieve readings from (Optional, if omitted all sensors will be selected)
 * @param metrics List of the metrics to retrieve readings from
 * @param aggregationType The type of aggregation of the metrics (Avg, Min, Max)
 * @param startTime The start of the range to measure (Optional, if omitted default range will be selected)
 * @param endTime The end of the range to measure (Optional, if omitted default range will be selected)
 */
public record ReadingQuery(
        List<@NotBlank String> sensorNames,
        List<@NotNull MetricType> metrics,
        AggregationType aggregationType,
        Instant startTime,
        Instant endTime) {

    public ReadingQuery {
        if (aggregationType == null) {
            aggregationType = AggregationType.AVG;
        }

        Instant now = Instant.now();
        if (startTime == null || endTime == null) {
            endTime = now;
            startTime = now.minus(Duration.ofDays(7));
        }
    }

    //Validated in query.
    @AssertTrue(message = "Start must be before end")
    public boolean isStartBeforeEnd() {
        return startTime != null && endTime != null && startTime.isBefore(endTime);
    }

}
