package com.e22e.moya.common.entity.npc;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "npc")
public class Npc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "npc_id", unique = true)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "npc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkNpcs> parkNpcs = new ArrayList<>();

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

    public List<ParkNpcs> getParkNpcs() {
        return parkNpcs;
    }

    public void setParkNpcs(List<ParkNpcs> parkNpcs) {
        this.parkNpcs = parkNpcs;
    }
}