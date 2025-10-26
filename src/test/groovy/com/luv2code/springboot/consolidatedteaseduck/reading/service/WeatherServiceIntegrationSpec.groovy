package com.luv2code.springboot.consolidatedteaseduck.reading.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import spock.lang.Specification


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeatherServiceIntegrationSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate
    @LocalServerPort
    Integer port

    def "Register two Sensors, Register Readings, Return Results"() {
        given:
        assert restTemplate != null: "TestRestTemplate not injected"
        assert port != null: "@LocalServerPort not injected"

        def base = "http://localhost:${port}"
        def sensor1 = [name: "bonham-1", location: "Galway"]
        def sensor2 = [name: "dock-1", location: "Galway Docks"]

        when:
        def responseSensor1 = restTemplate.postForEntity("${base}/api/v1/sensors", sensor1, Map)
        def responseSensor2 = restTemplate.postForEntity("${base}/api/v1/sensors", sensor2, Map)

        then:
        responseSensor1.statusCode == HttpStatus.CREATED
        responseSensor2.statusCode == HttpStatus.CREATED
        responseSensor1.body.name == "bonham-1"
        responseSensor2.body.name == "dock-1"

        when:
        def sensorsResponse = restTemplate.getForEntity("${base}/api/v1/sensors", List)

        then:
        def names = sensorsResponse.body*.name
        def locations = sensorsResponse.body*.location
        names == ["bonham-1", "dock-1"]
        locations == ["Galway", "Galway Docks"]

        when: "We post a few readings for those sensors"
        def r1 = [sensorName: "bonham-1", metricType: "TEMPERATURE", value: 500.5]
        def r2 = [sensorName: "bonham-1", metricType: "TEMPERATURE", value: 21.5]
        def r3 = [sensorName: "dock-1", metricType: "HUMIDITY", value: 55.0]

        def p1 = restTemplate.postForEntity("${base}/api/v1/readings", r1, Void)
        def p2 = restTemplate.postForEntity("${base}/api/v1/readings", r2, Void)
        def p3 = restTemplate.postForEntity("${base}/api/v1/readings", r3, Void)

        then: "All reading POSTs return CREATED"
        p1.statusCode == HttpStatus.CREATED
        p2.statusCode == HttpStatus.CREATED
        p3.statusCode == HttpStatus.CREATED

        when: "We call the aggregate endpoint with no params (defaults)"
        def aggRes = restTemplate.getForEntity("/api/v1/readings/aggregate", List)

        then:
        println "CONOR" + aggRes.toString()
    }
}

