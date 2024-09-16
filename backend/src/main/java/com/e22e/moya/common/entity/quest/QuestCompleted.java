package com.e22e.moya.common.entity.quest;

import com.e22e.moya.common.entity.Exploration;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Table(name = "quest_completed")
@ToString
public class QuestCompleted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exploration_id")
    @Exclude
    private Exploration exploration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    @Exclude
    private Quest quest;

    private LocalDateTime completedAt;

    @Column(name = "species_id")
    private Long speciesId;

    @Column(name = "npc_id")
    private Long npcId;

    private boolean completed;

    // getter, setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exploration getExploration() {
        return exploration;
    }

    public void setExploration(Exploration exploration) {
        this.exploration = exploration;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

    public Long getNpcId() {
        return npcId;
    }

    public void setNpcId(Long npcId) {
        this.npcId = npcId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
