package com.e22e.moya.common.entity.npc;

import com.e22e.moya.common.entity.chatting.Chat;
<<<<<<< HEAD
=======
import com.e22e.moya.common.entity.quest.Quest;
>>>>>>> 3c2a0e63c7b9f199925d1933d62250ec919940f3
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
<<<<<<< HEAD
    @JoinColumn(name = "park_npc_id")
    private ParkNpcs parkNpc;
=======
    @JoinColumn(name = "npc_id")
    private Npc npc;
>>>>>>> 3c2a0e63c7b9f199925d1933d62250ec919940f3

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

<<<<<<< HEAD

    public ParkNpcs getParkNpc() {
        return parkNpc;
    }

    public void setParkNpc(ParkNpcs parkNpc) {
        this.parkNpc = parkNpc;
=======
    public Npc getNpc() {
        return npc;
    }

    public void setNpc(Npc npc) {
        this.npc = npc;
>>>>>>> 3c2a0e63c7b9f199925d1933d62250ec919940f3
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
<<<<<<< HEAD

    public List<Long> getQuestIds() {
        return questIds;
    }

    public void setQuestIds(List<Long> questIds) {
        this.questIds = questIds;
    }

=======
>>>>>>> 3c2a0e63c7b9f199925d1933d62250ec919940f3
}
