package com.luv2code.springboot.consolidatedteaseduck.sensor.repository;


import com.luv2code.springboot.consolidatedteaseduck.sensor.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    boolean existsByNameIgnoreCase(String name);
    Optional<Sensor> findByNameIgnoreCase(String name);

//    @Query("select s.id from Sensor s where s.name in :names")
//    List<Long> findIdsByNames(@Param("names") List<String> names);
}
