package com.luv2code.springboot.consolidatedteaseduck.reading.service;

import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType;
import com.luv2code.springboot.consolidatedteaseduck.exception.UnknownSensorException;
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.AggregateResult;
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.CreateReadingRequest;
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.ReadingQuery;
import com.luv2code.springboot.consolidatedteaseduck.reading.entity.Reading;
import com.luv2code.springboot.consolidatedteaseduck.reading.repository.ReadingRepository;
import com.luv2code.springboot.consolidatedteaseduck.sensor.entity.Sensor;
import com.luv2code.springboot.consolidatedteaseduck.sensor.repository.SensorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadingService {

    private final ReadingRepository readingRepository;
    private final SensorRepository sensorRepository;


    @Transactional
    public Reading registerNewReading(final CreateReadingRequest createReadingRequest) {
        Sensor sensor = sensorRepository.findByNameIgnoreCase(createReadingRequest.sensorName())
                .orElseThrow(() -> new UnknownSensorException("Unknown sensor: " + createReadingRequest.sensorName()));

        Reading reading = new Reading();
        reading.setSensor(sensor);
        reading.setMetricType(createReadingRequest.metricType());
        reading.setValue(createReadingRequest.value());
        reading.setTimestamp(createReadingRequest.timestamp() != null ? createReadingRequest.timestamp() : Instant.now()); //date
        return readingRepository.save(reading);
    }

    @Transactional
    public List<Reading> getAllReadings() {
        return readingRepository.findAll();
    }

    @Transactional
    public List<AggregateResult> getAggregatedData(final ReadingQuery readingQuery) {

        final Map<Boolean,List<String>> sensorMapByIfExists = validateReadingQueryInputs(readingQuery);

        log.info("Getting Aggregated Data for sensors={}, metrics={}, agg={}, start={}, end={}",
                readingQuery.sensorNames(), readingQuery.metrics(), readingQuery.aggregationType(),
                readingQuery.startTime(), readingQuery.endTime());

        //Ommission for Sensor/Metric == All, Omission for aggregate == AVG
        List<String> sensorNames = (readingQuery.sensorNames() == null || readingQuery.sensorNames().isEmpty())
                ? sensorRepository.findAll().stream().map(Sensor::getName).collect(Collectors.toList()) : sensorMapByIfExists.get(true);

        List<String> metricNames = (readingQuery.metrics() == null || readingQuery.metrics().isEmpty())
                ? Arrays.stream(MetricType.values()).map(Enum::name).toList() : readingQuery.metrics().stream().map(Enum::name).toList();

        List<ReadingRepository.ReadingProjection> aggregatedResults = readingRepository.getAggregateResult(readingQuery.startTime(),
                readingQuery.endTime(), sensorNames, metricNames);

        return aggregatedResults.stream().map(ar -> new AggregateResult(
                ar.getSensorName(),
                MetricType.valueOf(ar.getMetricType()),
                readingQuery.aggregationType(),
                switch (readingQuery.aggregationType()) {
                    case AVG -> ar.getAvg();
                    case MIN -> ar.getMin();
                    case MAX -> ar.getMax();
                },
                ar.getCount(),
                readingQuery.startTime(),
                readingQuery.endTime()
        )).toList();
    }

    private Map<Boolean,List<String>> validateReadingQueryInputs(final ReadingQuery readingQuery) {
             return readingQuery.sensorNames()
                .stream()
                .collect(Collectors.partitioningBy(sensorRepository::existsByNameIgnoreCase));

    }
}

