package com.e22e.moya.diary.repository;

import com.e22e.moya.common.entity.Exploration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryExplorationRepositoryDiary extends JpaRepository<Exploration, Long> {

    /**
     * 사용자의 가장 최근 탐험 기록을 조회
     *
     * @param userId 사용자 ID
     * @return Exploration 최근 탐험 정보
     */
    Exploration findTopByUserIdOrderByStartTimeDesc(Long userId);

    /**
     * 사용자의 탐험 기록을 페이지네이션하여 조회
     *
     * @param userId 사용자 ID
     * @param pageable 페이지 요청 정보
     * @return Page<Exploration> 탐험 리스트 페이지
     */
    Page<Exploration> findByUserIdOrderByStartTimeDesc(Long userId, Pageable pageable);

    /**
     * 탐험 ID와 사용자 ID로 특정 탐험 조회
     *
     * @param explorationId 탐험 ID
     * @param userId 사용자 ID
     * @return Exploration 탐험 정보
     */
    Exploration findByIdAndUserId(Long explorationId, Long userId);
}
