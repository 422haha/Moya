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
    private long id;

    private String name;

    @OneToMany(mappedBy = "npc", cascade = CascadeType.ALL, orphanRemoval = true)
<<<<<<< HEAD
    private List<ParkNpcs> parkNpcs = new ArrayList<>();
=======
    private List<NpcPos> positions = new ArrayList<>();

    public void addPosition(NpcPos position) {
        positions.add(position);
        position.setNpc(this);
    }

    public void removePosition(NpcPos position) {
        positions.remove(position);
        position.setNpc(null);
    }
>>>>>>> 3c2a0e63c7b9f199925d1933d62250ec919940f3

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

<<<<<<< HEAD
    public List<ParkNpcs> getParkNpcs() {
        return parkNpcs;
    }

    public void setParkNpcs(List<ParkNpcs> parkNpcs) {
        this.parkNpcs = parkNpcs;
    }
=======
    public List<NpcPos> getPositions() {
        return positions;
    }

    public void setPositions(List<NpcPos> positions) {
        this.positions = positions;
    }

>>>>>>> 3c2a0e63c7b9f199925d1933d62250ec919940f3
}
