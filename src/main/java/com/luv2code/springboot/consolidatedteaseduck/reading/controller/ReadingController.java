package com.luv2code.springboot.consolidatedteaseduck.reading.controller;

import com.luv2code.springboot.consolidatedteaseduck.domain.AggregationType;
import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType;
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.AggregateResult;
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.CreateReadingRequest;
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.ReadingQuery;
import com.luv2code.springboot.consolidatedteaseduck.reading.entity.Reading;
import com.luv2code.springboot.consolidatedteaseduck.reading.service.ReadingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * GETs all readings
     *
     * @return A list of all the readings
     */
    @GetMapping
    List<Reading> getAllReadings() {
        return readingService.getAllReadings();
    }

    /**
     * POSTs a new sensor reading
     *
     * @param createReadingRequest
     * @return
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Reading submitSensorReading(@Valid @RequestBody CreateReadingRequest createReadingRequest) {
        return readingService.registerNewReading(createReadingRequest);
    }

    /**
     * GETs aggregated data for the filters supplies. Several defaults apply as documented below
     *
     * @param sensors List of Sensors to include in Aggregated Data (Null/Empty implies ALL sensors)
     * @param metrics List of the Metrics to include in Aggregated Data (Null/Empty implies all metrics)
     * @param aggregationType The AggregationType of the data. i.e. Min/Max/Average/Sum
     * @param start Start time of the data requested (If empty will default to period of last 7 days)
     * @param end End time of the data requested (If empty will default to period of last 7 days)
     *
     * @return Returns ResponseEntity containing the list of the Aggregated data grouped by metric type
     */
    @GetMapping("/aggregate")
    public ResponseEntity<List<AggregateResult>> aggregateGet(
            @RequestParam(required = false) List<String> sensors,
            @RequestParam(required = false) List<MetricType> metrics,
            @RequestParam(required = false) AggregationType aggregationType,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end
    ) {

        final ReadingQuery request = new ReadingQuery(sensors, metrics, aggregationType, start, end);
        return readingService.getAggregatedData(request);
    }

    /**
     * POSTs a JSON body of the filters to use when getting aggregated data
     *
     * @param readingQuery Reading query JSON body to send to the end point
     * @return
     */
    @PostMapping("/aggregate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AggregateResult>> getAggregateResultList(@Valid @RequestBody ReadingQuery readingQuery) {
        return readingService.getAggregatedData(readingQuery);
    }
}
