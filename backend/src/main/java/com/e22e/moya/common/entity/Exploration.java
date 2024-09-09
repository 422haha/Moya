package com.e22e.moya.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.GenericGenerator;
import org.locationtech.jts.geom.LineString;

@Entity
@Table(name = "Exploration")
public class Exploration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "exploration_id", unique = true)
    private long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(precision = 10, scale = 2)
    private int distance;

    private Integer steps;

    @Column(name = "startdate")
    private LocalDate startDate;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(name = "image_url_small", length = 512)
    private String imageUrlSmall;

    @Column(columnDefinition = "geography(LineString,4326)")
    private LineString route;
}
