package com.e22e.moya.exploration.repository;

import com.e22e.moya.common.entity.quest.QuestCompleted;
import com.e22e.moya.common.entity.quest.QuestStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestCompletedRepositoryExploration extends JpaRepository<QuestCompleted, Long> {

    @Query("SELECT COUNT(qc) FROM QuestCompleted qc WHERE qc.exploration.id = :explorationId AND qc.status = :status")
    int countCompletedQuestsByExplorationId(@Param("explorationId") Long explorationId,
        @Param("status") QuestStatus status);

    @Query("SELECT qc FROM QuestCompleted qc WHERE qc.exploration.id = :explorationId")
    List<QuestCompleted> findByExplorationId(@Param("explorationId") Long explorationId);
}
