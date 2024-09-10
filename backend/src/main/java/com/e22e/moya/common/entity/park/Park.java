package com.e22e.moya.common.entity.park;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.npc.Npc;
import com.e22e.moya.common.entity.npc.ParkNpcs;
import com.e22e.moya.common.entity.species.ParkSpecies;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "park")
public class Park {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "park")
    private List<Exploration> explorations = new ArrayList<>();

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkPos> entrances = new ArrayList<>();

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkNpcs> parkNpcs = new ArrayList<>();

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkSpecies> parkSpecies = new ArrayList<>();

    public void addExploration(Exploration exploration) {
        explorations.add(exploration);
        exploration.setPark(this);
    }

    public void removeExploration(Exploration exploration) {
        explorations.remove(exploration);
        exploration.setPark(null);
    }

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

    public List<Exploration> getExplorations() {
        return explorations;
    }

    public void setExplorations(List<Exploration> explorations) {
        this.explorations = explorations;
    }

    public List<ParkNpcs> getParkNpcs() {
        return parkNpcs;
    }

    public void setParkNpcs(List<ParkNpcs> parkNpcs) {
        this.parkNpcs = parkNpcs;
    }

    public List<ParkPos> getEntrances() {
        return entrances;
    }

    public void setEntrances(List<ParkPos> entrances) {
        this.entrances = entrances;
    }

    public List<ParkSpecies> getParkSpecies() {
        return parkSpecies;
    }

    public void setParkSpecies(List<ParkSpecies> parkSpecies) {
        this.parkSpecies = parkSpecies;
    }
}
