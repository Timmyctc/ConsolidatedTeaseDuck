package com.luv2code.springboot.consolidatedteaseduck.reading.controller;

import com.luv2code.springboot.consolidatedteaseduck.domain.AggregationType;
import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType;
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.AggregateResult;
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.CreateReadingRequest;
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.ReadingRequest;
import com.luv2code.springboot.consolidatedteaseduck.reading.service.ReadingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/api/v1/readings")
public class ReadingController {

    private final ReadingService readingService;

    public ReadingController(ReadingService readingService) {
        this.readingService = readingService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void submitSensorReading(@Valid @RequestBody CreateReadingRequest createReadingRequest) {
        readingService.registerNewReading(createReadingRequest);
    }

    @PostMapping("/aggregate")
    @ResponseStatus(HttpStatus.OK)
    public List<AggregateResult> getAggregateResultList(@Valid @RequestBody ReadingRequest readingRequest) {
        return readingService.aggregate(readingRequest);
    }

    @GetMapping("/aggregate")
    public List<AggregateResult> aggregateGet(
            @RequestParam(required = false) List<String> sensors,
            @RequestParam(required = false) List<MetricType> metrics,
            @RequestParam(required = false) AggregationType aggregationType,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end
    ) {
        log.info("GET /aggregate called with sensors={}, metrics={}, aggregationType={}, start={}, end={}",
                sensors, metrics, aggregationType, start, end);

        ReadingRequest req = new ReadingRequest(sensors, metrics, aggregationType, start, end);
        return readingService.aggregate(req);
    }


}
