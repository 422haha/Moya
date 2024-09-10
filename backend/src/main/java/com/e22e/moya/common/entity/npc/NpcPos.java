package com.e22e.moya.common.entity.npc;

import com.e22e.moya.common.entity.chatting.Chat;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "npc_pos")
public class NpcPos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_npc_id")
    private ParkNpcs parkNpc;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "npc_pos_quests", joinColumns = @JoinColumn(name = "npc_pos_id"))
    @Column(name = "quest_id")
    private List<Long> questIds = new ArrayList<>();

    //getter, setter

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
