package com.e22e.moya.common.entity;

import com.e22e.moya.common.entity.species.Species;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.locationtech.jts.geom.Point;

// 사용자가 수집한 동식물
@Entity
@Table(name = "discovery")
public class Discovery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Point pos;

    private LocalDateTime discoverytTime;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id")
    private Species species;

    // getter, setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public LocalDateTime getDiscoverytTime() {
        return discoverytTime;
    }

    public void setDiscoverytTime(LocalDateTime discoverytTime) {
        this.discoverytTime = discoverytTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }
}
