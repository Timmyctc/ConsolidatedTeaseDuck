package com.luv2code.springboot.consolidatedteaseduck.service;

import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType;
import com.luv2code.springboot.consolidatedteaseduck.dto.AggregateResult;
import com.luv2code.springboot.consolidatedteaseduck.dto.ReadingRequest;
import com.luv2code.springboot.consolidatedteaseduck.repository.ReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadingAggregateService {

    private ReadingRepository repository;

    public ReadingAggregateService(ReadingRepository repository) {
        this.repository = repository;
    }
    public List<AggregateResult> aggregate(ReadingRequest request) {
        List<String> sensorNames = (request.sensorNames() == null || request.sensorNames().isEmpty())
                ? null
                : request.sensorNames();

        List<String> metrics = (request.metrics() == null || request.metrics().isEmpty())
                ? null
                : request.metrics().stream().map(Enum::name).toList();

        var rows = repository.aggregate(
                request.startTime(),
                request.endTime(),
                sensorNames,
                metrics
        );

        return rows.stream().map(r -> new AggregateResult(
                r.getSensorName(),
                MetricType.valueOf(r.getMetricType()),
                request.aggregationType(),
                switch (request.aggregationType()) {
                    case AVG -> r.getAvg();
                    case MIN -> r.getMin();
                    case MAX -> r.getMax();
                },
                r.getCount(),
                request.startTime(),
                request.endTime()
        )).toList();
    }
}
