package com.luv2code.springboot.consolidatedteaseduck.reading.service;

import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType;
import com.luv2code.springboot.consolidatedteaseduck.exception.UnknownSensorException;
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.AggregateResult;
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.CreateReadingRequest;
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.ReadingRequest;
import com.luv2code.springboot.consolidatedteaseduck.reading.entity.Reading;
import com.luv2code.springboot.consolidatedteaseduck.reading.repository.ReadingRepository;
import com.luv2code.springboot.consolidatedteaseduck.sensor.entity.Sensor;
import com.luv2code.springboot.consolidatedteaseduck.sensor.repository.SensorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadingService {

    private final ReadingRepository readingRepository;
    private final SensorRepository sensorRepository;


    @Transactional
    public void registerNewReading(final CreateReadingRequest createReadingRequest) {
        Sensor sensor = sensorRepository.findByNameIgnoreCase(createReadingRequest.sensorName())
                .orElseThrow(() -> new UnknownSensorException("Unknown sensor: " + createReadingRequest.sensorName()));

        Reading reading = new Reading();
        reading.setSensor(sensor);
        reading.setMetricType(createReadingRequest.metricType());
        reading.setValue(createReadingRequest.value());
        reading.setTimestamp(createReadingRequest.timestamp() != null ? createReadingRequest.timestamp() : Instant.now());
        readingRepository.save(reading);
    }

    @Transactional
    public List<AggregateResult> aggregate(final ReadingRequest readingRequest) {

        log.info("Aggregating for sensors={}, metrics={}, agg={}, start={}, end={}",
                readingRequest.sensorNames(), readingRequest.metrics(), readingRequest.aggregationType(),
                readingRequest.startTime(), readingRequest.endTime());

        //Ommission for Sensor/Metric == All, Omission for aggregate == AVG
        List<String> sensorNames = (readingRequest.sensorNames() == null || readingRequest.sensorNames().isEmpty())
                ? null : readingRequest.sensorNames();

        List<String> metricNames = (readingRequest.metrics() == null || readingRequest.metrics().isEmpty())
                ? null : readingRequest.metrics().stream().map(Enum::name).toList();

        List<ReadingRepository.ReadingProjection> aggregatedResults = readingRepository.getAggregateResult(readingRequest.startTime(),
                readingRequest.endTime(), sensorNames, metricNames);

        return aggregatedResults.stream().map(r -> new AggregateResult(
                r.getSensorName(),
                MetricType.valueOf(r.getMetricType()),
                readingRequest.aggregationType(),
                switch (readingRequest.aggregationType()) {
                    case AVG -> r.getAvg();
                    case MIN -> r.getMin();
                    case MAX -> r.getMax();
                },
                r.getCount(),
                readingRequest.startTime(),
                readingRequest.endTime()
        )).toList();

    }
}

