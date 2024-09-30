package com.e22e.moya.common.entity.species;

import com.e22e.moya.common.entity.Discovery;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.ToString;
import lombok.ToString.Exclude;

// 공원에 있는 동식물
@Entity
@Table(name = "species")
@ToString
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "species_id", unique = true)
    private Long id;

    private String name;

    private String scientificName;

    private String description;

    private String imageUrl;

    @ElementCollection(targetClass = Season.class)
    @CollectionTable(name = "species_seasons", joinColumns = @JoinColumn(name = "species_id"))
    @Column(name = "season")
    @Enumerated(EnumType.STRING)
    private Set<Season> visibleSeasons;

    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL, orphanRemoval = true)
    @Exclude
    private List<Discovery> discoveries = new ArrayList<>();

    @OneToMany(mappedBy = "species", cascade = CascadeType.ALL, orphanRemoval = true)
    @Exclude
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

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
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

    public Set<Season> getVisibleSeasons() {
        return visibleSeasons;
    }

    public void setVisibleSeasons(Set<Season> visibleSeasons) {
        this.visibleSeasons = visibleSeasons;
    }
}
