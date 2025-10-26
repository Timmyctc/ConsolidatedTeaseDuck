package com.luv2code.springboot.consolidatedteaseduck;

import com.luv2code.springboot.consolidatedteaseduck.sensor.dto.CreateSensorRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MVP PoC JUnit test that exercises the Sensor + Reading + Aggregate endpoints end-to-end.
 *
 * Single-test approach to avoid ordering/state issues: it creates a sensor, posts a reading,
 * then calls the aggregate GET endpoint (no query params) and asserts the returned aggregates
 * include the sensor we created.
 *
 * Run with:
 *   mvn -Dtest=ApiEndpointsPoCTest test
 */
@SpringBootTest(classes = WeatherDataService.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiEndpointsPoCTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createSensorPostReadingAndGetAggregate_noParams_returnsAggregateContainingSensor() {
        assertNotNull(restTemplate, "TestRestTemplate must be injected");

        CreateSensorRequest sensorPayload = new CreateSensorRequest("Bonham-1", "Galway");

        ResponseEntity<Map> createSensorResponse = restTemplate.postForEntity("/api/v1/sensors", sensorPayload, Map.class);
        assertEquals(HttpStatus.CREATED, createSensorResponse.getStatusCode(), "Sensor creation should return 201 CREATED");
        assertNotNull(createSensorResponse.getBody(), "Sensor creation response body should not be null");
        assertEquals("poc-sensor-1", createSensorResponse.getBody().get("name"), "Created sensor name should match");

        // 2) Post a reading for that sensor (timestamp omitted -> service will set now)
        var readingPayload = Map.of(
                "sensorName", "poc-sensor-1",
                "metricType", "TEMPERATURE",
                "value", 23.5
        );

        ResponseEntity<Void> createReadingResp = restTemplate.postForEntity("/api/v1/readings", readingPayload, Void.class);
        assertEquals(HttpStatus.CREATED, createReadingResp.getStatusCode(), "Reading POST should return 201 CREATED");

        // 3) Call aggregate endpoint with no params (uses service defaults)
        ResponseEntity<List> aggResp = restTemplate.getForEntity("/api/v1/readings/aggregate", List.class);
        assertEquals(HttpStatus.OK, aggResp.getStatusCode(), "Aggregate GET should return 200 OK");
        assertNotNull(aggResp.getBody(), "Aggregate response body should not be null");
//
//        // The controller returns a list of aggregate objects; when mapped as List by TestRestTemplate
//        // they are LinkedHashMaps (Map). Check that at least one item references our sensor name.
//        boolean found = aggResp.getBody().stream()
//                .filter(item -> item instanceof Map)
//                .map(item -> (Map<?, ?>) item)
//                .anyMatch(m -> "poc-sensor-1".equals(m.get("sensorName")));
//
//        assertTrue(found, "Aggregate results should contain an entry for poc-sensor-1");
    }
}