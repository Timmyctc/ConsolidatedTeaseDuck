package com.luv2code.springboot.consolidatedteaseduck.entity;

import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "readings")
@Getter
@Setter
@NoArgsConstructor
public class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //Sensor that made the reading, one sensor can make many readings
    @ManyToOne(optional = false)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(nullable = false)
    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetricType metricType;

    @Column(nullable = false)
    private double value;

//    private double temperature;
//    private double humidity;
//    private int windSpeed;
}
