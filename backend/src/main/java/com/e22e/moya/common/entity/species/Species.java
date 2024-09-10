package com.e22e.moya.common.entity.species;

import com.e22e.moya.common.entity.Discovery;
import com.e22e.moya.common.entity.park.ParkSpecies;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// 공원에 있는 동식물
@Entity
@Table(name = "species")
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String scientific_name;

    private String description;

    private String imageUrl;

    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpeciesPos> positions = new ArrayList<>();

    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discovery> discoveries = new ArrayList<>();

    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkSpecies> parkSpecies = new ArrayList<>();

    //getter, setter

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<SpeciesPos> getPositions() {
        return positions;
    }

    public void setPositions(List<SpeciesPos> positions) {
        this.positions = positions;
    }

    public List<Discovery> getDiscoveries() {
        return discoveries;
    }

    public void setDiscoveries(List<Discovery> discoveries) {
        this.discoveries = discoveries;
    }
}
