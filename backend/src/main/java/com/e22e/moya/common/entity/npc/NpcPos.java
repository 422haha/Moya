package com.e22e.moya.common.entity.npc;

import com.e22e.moya.common.entity.chatting.Chat;
import jakarta.persistence.*;
import lombok.ToString;
import lombok.ToString.Exclude;
import java.util.ArrayList;
import java.util.List;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

@Entity
@Table(name = "npc_pos")
@ToString
public class NpcPos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사실상 npc id

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point<G2D> pos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_npc_id")
    @Exclude
    private ParkNpcs parkNpc;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Exclude
    private List<Chat> chats = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "npc_pos_quests", joinColumns = @JoinColumn(name = "npc_pos_id"))
    @Column(name = "quest_id")
    private List<Long> questIds = new ArrayList<>();

    //getter, setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Point<G2D> getPos() {
        return pos;
    }

    public void setPos(Point<G2D> pos) {
        this.pos = pos;
    }

    public ParkNpcs getParkNpc() {
        return parkNpc;
    }

    public void setParkNpc(ParkNpcs parkNpc) {
        this.parkNpc = parkNpc;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public List<Long> getQuestIds() {
        return questIds;
    }

    public void setQuestIds(List<Long> questIds) {
        this.questIds = questIds;
    }

}
