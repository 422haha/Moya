package com.e22e.moya.exploration.repository;

import com.e22e.moya.common.entity.quest.QuestCompleted;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestCompletedRepository extends JpaRepository<QuestCompleted, Long> {

    List<QuestCompleted> findByExplorationUserIdAndExplorationId(long userId, Long explorationId);

    @Query("SELECT COUNT(qc) FROM QuestCompleted qc WHERE qc.exploration.id = :explorationId AND qc.completed = true")
    int countCompletedQuestsByExplorationId(@Param("explorationId") Long explorationId);
}
