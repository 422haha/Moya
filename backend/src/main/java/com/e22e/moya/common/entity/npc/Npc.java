package com.e22e.moya.common.entity.npc;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Npc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "npc_id", unique = true)
    private long id;

    private String name;

    @OneToMany(mappedBy = "npc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NpcPos> positions = new ArrayList<>();

    public void addPosition(NpcPos position) {
        positions.add(position);
        position.setNpc(this);
    }

    public void removePosition(NpcPos position) {
        positions.remove(position);
        position.setNpc(null);
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

    public List<NpcPos> getPositions() {
        return positions;
    }

    public void setPositions(List<NpcPos> positions) {
        this.positions = positions;
    }

}
