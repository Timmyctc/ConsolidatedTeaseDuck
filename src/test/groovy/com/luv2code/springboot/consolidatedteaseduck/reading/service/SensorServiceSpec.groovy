package com.luv2code.springboot.consolidatedteaseduck.sensor.service

import com.luv2code.springboot.consolidatedteaseduck.exception.DuplicateSensorNameException
import com.luv2code.springboot.consolidatedteaseduck.sensor.dto.CreateSensorRequest
import com.luv2code.springboot.consolidatedteaseduck.sensor.entity.Sensor
import com.luv2code.springboot.consolidatedteaseduck.sensor.repository.SensorRepository
import spock.lang.Specification

class SensorServiceSpec extends Specification {

    SensorRepository sensorRepository = Mock()

    SensorService sensorService = new SensorService(sensorRepository)

    def "registerNewSensor saves and returns Sensor when name is unique"() {
        given: "a createSensorRequest and unique name"
        def request = new CreateSensorRequest("Bonham-Quay-1", "roof-top")
        sensorRepository.existsByNameIgnoreCase("bonham-quay-1") >> false

        when: "registerNewSensor is called"
        def result = sensorService.registerNewSensor(request)

        then: "repository.save is invoked"
        1 * sensorRepository.save({ Sensor s ->
                    s.name == "Bonham-Quay-1" &&
                    s.location == "roof-top"})
    }

    def "registerNewSensor throws DuplicateSensorNameException when name already exists"() {
        given:
        def createSensorRequest = new CreateSensorRequest("existing-sensor", "basement")
        sensorRepository.existsByNameIgnoreCase("existing-sensor") >> true

        when:
        sensorService.registerNewSensor(createSensorRequest)

        then:
        thrown(DuplicateSensorNameException)
        0 * sensorRepository.save(_)
    }

    def "getAllSensors returns repository results"() {
        given: "repository contains two sensors"
        def s1 = new Sensor()
        s1.setName("sensor-1")
        def s2 = new Sensor()
        s2.setName("sensor-2")
        sensorRepository.findAll() >> [s1, s2]

        when: "getAllSensors is called"
        def all = sensorService.getAllSensors()

        then: "all sensors are retuerned"
        all.size() == 2
        all.collect { it.name }.containsAll(["sensor-1", "sensor-2"])
    }
}