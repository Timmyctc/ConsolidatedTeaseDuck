package com.luv2code.springboot.consolidatedteaseduck.repository;


import com.luv2code.springboot.consolidatedteaseduck.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    @Query("select s.id from Sensor s where s.name in :names")
    List<Long> findIdsByNames(@Param("names") List<String> names);
}
