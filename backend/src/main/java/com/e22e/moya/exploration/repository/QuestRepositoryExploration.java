package com.e22e.moya.exploration.repository;

import com.e22e.moya.common.entity.quest.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestRepositoryExploration extends JpaRepository<Quest, Integer> {

}
