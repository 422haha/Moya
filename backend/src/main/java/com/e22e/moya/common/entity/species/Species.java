package com.e22e.moya.common.entity.species;

import com.e22e.moya.common.entity.Discovery;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// 공원에 있는 동식물
@Entity
@Table(name = "species")
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "species_id", unique = true)
    private Long id;

    private String name;

    private String scientific_name;

    private String description;

    private String imageUrl;

    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discovery> discoveries = new ArrayList<>();

    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkSpecies> parkSpecies = new ArrayList<>();

    //getter, setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Discovery> getDiscoveries() {
        return discoveries;
    }

    public void setDiscoveries(List<Discovery> discoveries) {
        this.discoveries = discoveries;
    }

    public List<ParkSpecies> getParkSpecies() {
        return parkSpecies;
    }

    public void setParkSpecies(List<ParkSpecies> parkSpecies) {
        this.parkSpecies = parkSpecies;
    }
}
