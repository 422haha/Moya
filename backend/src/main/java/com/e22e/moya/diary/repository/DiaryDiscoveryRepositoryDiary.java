package com.e22e.moya.diary.repository;

import com.e22e.moya.common.entity.Discovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryDiscoveryRepositoryDiary extends JpaRepository<Discovery, Long> {

    /**
     * 탐험 ID를 사용하여 Discovery 정보 조회
     *
     * @param explorationId 탐험 ID
     * @return List<Discovery> 탐험에서 수집된 Discovery 목록
     */
    @Query("SELECT d FROM Discovery d WHERE d.speciesPos.parkSpecies.park.id = :explorationId")
    List<Discovery> findByExplorationId(@Param("explorationId") Long explorationId);
}
