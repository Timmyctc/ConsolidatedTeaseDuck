package com.luv2code.springboot.consolidatedteaseduck.sensor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Sensor entity. Has a generated ID for PK but also a uniquely identifable name
 */
@Entity @Table(name = "sensors")
@Getter
@Setter
@NoArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Assuming one sensor covers a named area
    @Column(nullable = false, unique = true, length = 64)
    private String name;

    private String location;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;
}
