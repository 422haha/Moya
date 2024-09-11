package com.e22e.moya.common.entity.quest;

import com.e22e.moya.common.entity.Exploration;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quest_completed")
public class QuestCompleted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exploration_id")
    private Exploration exploration;

    @JoinColumn(name = "quest_id")
    private long questId;

    @Column(nullable = false)
    private LocalDateTime completedAt;

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

    public long getQuestId() {
        return questId;
    }

    public void setQuestId(long questId) {
        this.questId = questId;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
