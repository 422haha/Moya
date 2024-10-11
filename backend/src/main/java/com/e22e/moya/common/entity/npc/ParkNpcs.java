package com.e22e.moya.common.entity.npc;

import com.e22e.moya.common.entity.park.Park;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Table(name = "park_npc")
@ToString
public class ParkNpcs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id")
    @Exclude
    private Park park;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "npc_id")
    @Exclude
    private Npc npc;

    @OneToMany(mappedBy = "parkNpc", cascade = CascadeType.ALL, orphanRemoval = true)
    @Exclude
    private List<NpcPos> positions = new ArrayList<>();

    public void addPosition(NpcPos position) {
        positions.add(position);
        position.setParkNpc(this);
    }

    public void removePosition(NpcPos position) {
        positions.remove(position);
        position.setParkNpc(null);
    }

    // getter, setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }

    public Npc getNpc() {
        return npc;
    }

    public void setNpc(Npc npc) {
        this.npc = npc;
    }

    public List<NpcPos> getPositions() {
        return positions;
    }

    public void setPositions(List<NpcPos> positions) {
        this.positions = positions;
    }

}
