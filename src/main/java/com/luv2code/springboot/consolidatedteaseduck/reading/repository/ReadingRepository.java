package com.luv2code.springboot.consolidatedteaseduck.reading.repository;

import com.luv2code.springboot.consolidatedteaseduck.reading.entity.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ReadingRepository extends JpaRepository<Reading, Long> {

    //https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html

    interface ReadingProjection {
        String getMetricType();
        Long getCount();
        Double getAvg();
        Double getMin();
        Double getMax();
    }

    @Query(value = """
      SELECT r.metric_type AS metricType,
             COUNT(*)      AS count,
             AVG(r.reading_value)  AS avg,
             MIN(r.reading_value)  AS min,
             MAX(r.reading_value)  AS max
      FROM readings r
      JOIN sensors s ON s.id = r.sensor_id
      WHERE r.recorded_at >= :start AND r.recorded_at < :end
        AND (:sensorNames IS NULL OR s.name IN (:sensorNames))
        AND (:metrics     IS NULL OR r.metric_type IN (:metrics))
      GROUP BY r.metric_type
      """, nativeQuery = true)
    List<ReadingProjection> getAggregateResult(
            @Param("start") Instant start,
            @Param("end")   Instant end,
            @Param("sensorNames") List<String> sensorNames,
            @Param("metrics") List<String> metrics
    );
}
