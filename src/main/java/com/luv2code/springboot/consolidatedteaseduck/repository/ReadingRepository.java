package com.luv2code.springboot.consolidatedteaseduck.repository;

import com.luv2code.springboot.consolidatedteaseduck.entity.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ReadingRepository extends JpaRepository<Reading, Long> {

    //inner projection maps to the custom query below
    //https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html
    interface AggregateRow {
        String getSensorName();
        String getMetricType();
        Long getCount();
        Double getAvg();
        Double getMin();
        Double getMax();
    }

    @Query(value = """
      SELECT s.name        AS sensorName,
             r.metric_type AS metricType,
             COUNT(*)      AS count,
             AVG(r.value)  AS avg,
             MIN(r.value)  AS min,
             MAX(r.value)  AS max
      FROM readings r
      JOIN sensors s ON s.id = r.sensor_id
      WHERE r.timestamp >= :start AND r.timestamp < :end
        AND (:sensorNamesEmpty OR s.name IN (:sensorNames))
        AND (:metricsEmpty     OR r.metric_type IN (:metrics))
      GROUP BY s.name, r.metric_type
      """, nativeQuery = true)
    List<AggregateRow> aggregate(
            @Param("start") Instant start,
            @Param("end")   Instant end,
            @Param("sensorNames") List<String> sensorNames,
            @Param("metrics") List<String> metrics,
    );
}
