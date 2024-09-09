package com.e22e.moya.common.entity;

import com.e22e.moya.common.entity.npc.Npc;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Park {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "park")
    private List<Exploration> explorations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "park_id")
    private List<Npc> npcs = new ArrayList<>();

    public void addExploration(Exploration exploration) {
        explorations.add(exploration);
        exploration.setPark(this);
    }

    public void removeExploration(Exploration exploration) {
        explorations.remove(exploration);
        exploration.setPark(null);
    }

    public void addNpc(Npc npc) {
        npcs.add(npc);
    }

    public void removeNpc(Npc npc) {
        npcs.remove(npc);
    }

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

    public List<Npc> getNpcs() {
        return npcs;
    }

    public void setNpcs(List<Npc> npcs) {
        this.npcs = npcs;
    }
}
