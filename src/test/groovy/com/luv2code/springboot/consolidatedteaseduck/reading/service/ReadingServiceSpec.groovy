package com.luv2code.springboot.consolidatedteaseduck.reading.service

import com.luv2code.springboot.consolidatedteaseduck.reading.repository.ReadingRepository
import org.springframework.data.geo.Metric
import spock.lang.Specification

import java.time.Instant

class ReadingServiceSpec extends Specification {

    def readingRepo = Mock(ReadingRepository)
    def readingService = new ReadingService(readingRepo)

    def "defaults to AVG and 24h when omitted"() {
        given:
        Set<String> sensorIds = [] as Set
        Set<Metric> metrics   = [] as Set

        when:
        def out = readingService.aggregate(sensorIds, metrics, null, null, null)

        then:
        out.get(0).metricType()



    def "RegisterNewReading"() {
    }

    def "Aggregate"() {
    }
}
