package com.e22e.moya.diary.repository;

import com.e22e.moya.common.entity.Discovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryDiscoveryRepositoryDiary extends JpaRepository<Discovery, Long> {

    /**
     * 사용자 ID와 탐험 ID를 사용하여 Discovery 정보 조회
     *
     * @param userId 사용자 ID
     * @param explorationId 탐험 ID
     * @return List<Discovery> 탐험에서 수집된 Discovery 목록
     */
    @Query("SELECT d FROM Discovery d WHERE d.userId = :userId AND d.speciesPos.parkSpecies.park.id = :explorationId")
    List<Discovery> findByUserIdAndExplorationId(@Param("userId") Long userId, @Param("explorationId") Long explorationId);
}
