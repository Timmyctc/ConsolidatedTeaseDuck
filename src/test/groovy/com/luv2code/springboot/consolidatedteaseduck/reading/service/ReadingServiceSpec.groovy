package com.luv2code.springboot.consolidatedteaseduck.reading.service

import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType
import com.luv2code.springboot.consolidatedteaseduck.exception.UnknownSensorException
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.AggregateResult
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.CreateReadingRequest
import com.luv2code.springboot.consolidatedteaseduck.reading.dto.ReadingQuery
import com.luv2code.springboot.consolidatedteaseduck.reading.entity.Reading
import com.luv2code.springboot.consolidatedteaseduck.reading.repository.ReadingRepository
import com.luv2code.springboot.consolidatedteaseduck.sensor.entity.Sensor
import com.luv2code.springboot.consolidatedteaseduck.sensor.repository.SensorRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import spock.lang.Specification

import java.time.Instant

class ReadingServiceSpec extends Specification {

    def readingRepo = Mock(ReadingRepository)
    def sensorRepo = Mock(SensorRepository)

    ReadingService readingService = new ReadingService(readingRepo, sensorRepo)

    def 'registerNewReading saves a Reading when sensor exists'() {
        given: 'A valid CreateReadingRequest including valid sensor'
        def sensor = new Sensor()
        sensor.setName("sensor-1")
        def request = new CreateReadingRequest("sensor-1", MetricType.TEMPERATURE, 21.0d, null)

        sensorRepo.findByNameIgnoreCase("sensor-1") >> Optional.of(sensor)

        when: 'registerNewReading is called'
        readingService.registerNewReading(request)

        then:
        1 * readingRepo.save({ Reading r ->
                    r.sensor == sensor &&
                    r.metricType == request.metricType() &&
                    r.value == request.value()
        })
    }

    def 'registerNewReading throws an error when sensor does not exist'() {
        given: 'A valid CreateReadingRequest including valid sensor'
        def request = new CreateReadingRequest("bad-sensor", MetricType.TEMPERATURE, 21.0d, null)
        sensorRepo.findByNameIgnoreCase("bad-sensor") >> Optional.empty()

        when: 'registerNewReading is called'
        readingService.registerNewReading(request)

        then:
        thrown(UnknownSensorException)
    }

    def 'getAggregatedData returns 404 Not Found when no valid sensors are specified'() {
        given: 'A ReadingQuery with invalid sensor information'
        def readingQuery = new ReadingQuery(List.of("bad","bad", "leeroy","brown"),null,null,null,null)

        when: 'getAggregatedData is called'
        readingService.getAggregatedData(readingQuery)
        def headers = new HttpHeaders()
        headers.add("Warning", "No valid sensors specified")

        then: '404 Resource Not Found is returned'
        assert readingService.getAggregatedData(readingQuery) == new ResponseEntity(headers, HttpStatus.NOT_FOUND)
    }

    def 'Headers contain warning that Some Sensors were invalid when user specifies mix of valid and invalid sensors'() {
        given: 'A ReadingQuery with some invalid sensor information'

        def readingQuery = new ReadingQuery(List.of("sensor-1","bad", "leeroy","brown"),null,null,null,null)
        def sensor = new Sensor()
        sensor.setName("sensor-1")
        sensorRepo.existsByNameIgnoreCase("sensor-1") >> true

       readingRepo.getAggregateResult(_ , _ , _ , _ ) >> []

        when: 'getAggregatedData is invoked'
        ResponseEntity<List<AggregateResult>> response = readingService.getAggregatedData(readingQuery)

        then: "Headers contain warning about some invalid sensors"
        response.statusCode == HttpStatus.OK
        response.headers.getFirst("Warning") == "Some Sensors were invalid"
    }
}

