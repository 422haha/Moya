package com.e22e.moya.common.entity;

import com.e22e.moya.common.entity.species.Species;
import jakarta.persistence.*;
import java.time.LocalDateTime;

// 사용자가 수집한 동식물
@Entity
public class Discovery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double latitude;

    private double longitude;

    private LocalDateTime discoverytTime;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id")
    private Species species;

    // getter, setter

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
