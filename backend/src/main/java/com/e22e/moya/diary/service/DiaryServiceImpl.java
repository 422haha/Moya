package com.e22e.moya.diary.service;

import com.e22e.moya.common.entity.Discovery;
import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.quest.QuestStatus;
import com.e22e.moya.common.s3Service.PresignedUrlService;
import com.e22e.moya.diary.dto.*;
import com.e22e.moya.diary.repository.DiaryDiscoveryRepositoryDiary;
import com.e22e.moya.diary.repository.DiaryExplorationRepositoryDiary;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.geolatte.geom.G2D;
import org.geolatte.geom.LineString;
import org.geolatte.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryExplorationRepositoryDiary explorationRepository;
    private final DiaryDiscoveryRepositoryDiary discoveryRepository;
    private final PresignedUrlService presignedUrlService;

    /**
     * 사용자의 가장 최근 탐험 정보를 조회
     *
     * @param userId 사용자 ID
     * @return 최근 탐험 정보
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public DiaryHomeResponseDto getLatestExploration(Long userId) {
        // 사용자의 가장 최근 탐험 기록을 조회
        Exploration exploration = explorationRepository.findTopByUserIdAndCompletedTrueOrderByStartTimeDesc(
            userId);

        // 탐험 기록이 없으면 예외 발생
        if (exploration == null) {
            throw new EntityNotFoundException("최근 탐험을 찾을 수 없습니다.");
        }

        DiaryHomeResponseDto responseDto = new DiaryHomeResponseDto();
        responseDto.setParkId(exploration.getPark().getId());
        responseDto.setExplorationId(exploration.getId());
        responseDto.setStartDate(exploration.getStartDate());
        responseDto.setParkName(exploration.getPark().getName());
        responseDto.setImageUrl(presignedUrlService.getPresignedUrl(exploration.getPark().getImageUrl()));

        return responseDto;
    }

    /**
     * 사용자의 탐험 리스트를 페이지네이션하여 조회
     *
     * @param userId 사용자 ID
     * @param page   페이지 번호
     * @param size   페이지 크기
     * @return 탐험 리스트
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public DiaryListResponseDto getExplorations(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        List<Exploration> explorations = explorationRepository
            .findByUserIdAndCompletedTrueOrderByStartTimeDesc(userId, pageRequest)
            .getContent();

        List<DiaryExplorationDto> explorationDtos = explorations.stream()
            .map(exploration -> {
                DiaryExplorationDto dto = new DiaryExplorationDto();
                dto.setExplorationId(exploration.getId());
                dto.setParkName(exploration.getPark().getName());
                dto.setStartTime(exploration.getStartTime());
                dto.setDistance(exploration.getDistance());

                // 공원의 이미지 URL 설정
                dto.setImageUrl(presignedUrlService.getPresignedUrl(exploration.getPark().getImageUrl()));

                // 수집된 동식물 수
                List<Discovery> discoveries = discoveryRepository.findByExplorationId(exploration.getId());
                dto.setCollectedCount(discoveries.size());

                // 소요 시간 계산
                if (exploration.getEndTime() != null && exploration.getStartTime() != null) {
                    long duration = Duration.between(exploration.getStartTime(), exploration.getEndTime()).toMinutes();
                    dto.setDuration((int) duration);
                } else {
                    dto.setDuration(0);
                }

                // QuestStatus가 COMPLETE인 퀘스트만 카운트
                long questCompletedCount = exploration.getQuestCompleted().stream()
                    .filter(quest -> quest.getStatus() == QuestStatus.COMPLETE)
                    .count();
                dto.setQuestCompletedCount((int) questCompletedCount);

                return dto;
            })
            .collect(Collectors.toList());

        DiaryListResponseDto responseDto = new DiaryListResponseDto();
        responseDto.setExplorations(explorationDtos);

        return responseDto;
    }

    /**
     * 특정 탐험 ID를 기반으로 탐험의 상세 정보를 조회
     *
     * @param userId        사용자 ID
     * @param explorationId 탐험 ID
     * @return 탐험 상세 정보
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public DiaryDetailResponseDto getExplorationDetail(Long userId, Long explorationId) {
        // explorationId를 기반으로 탐험을 조회
        Exploration exploration = explorationRepository.findByIdAndUserId(explorationId, userId);

        // 탐험이 존재하지 않으면 예외 발생
        if (exploration == null) {
            throw new EntityNotFoundException("탐험을 찾을 수 없습니다.");
        }

        DiaryDetailResponseDto responseDto = new DiaryDetailResponseDto();
        responseDto.setExplorationId(exploration.getId());
        responseDto.setParkName(exploration.getPark().getName());
        responseDto.setStartTime(exploration.getStartTime());
        responseDto.setEndTime(exploration.getEndTime());
        responseDto.setDistance(exploration.getDistance());
        responseDto.setSteps(exploration.getSteps());

        // 이동 경로 설정
        LineString<G2D> route = exploration.getRoute();
        if (route != null) {
            List<DiaryRouteDto> routeDtos = new ArrayList<>();
            for (int i = 0; i < route.getNumPositions(); i++) {
                G2D position = route.getPositionN(i);
                DiaryRouteDto routeDto = new DiaryRouteDto();
                routeDto.setLatitude(position.getLat());
                routeDto.setLongitude(position.getLon());
                routeDtos.add(routeDto);
            }
            responseDto.setRoute(routeDtos);
        }

        // explorationId를 통해 Discovery 정보를 조회
        List<Discovery> discoveries = discoveryRepository.findByExplorationId(explorationId);
        List<DiaryCollectedDto> collectedDtos = discoveries.stream()
            .map(discovery -> {
                DiaryCollectedDto dto = new DiaryCollectedDto();
                dto.setSpeciesId(discovery.getSpecies().getId());
                dto.setSpeciesName(discovery.getSpecies().getName());
                dto.setImageUrl(presignedUrlService.getPresignedUrl(discovery.getImageUrl()));

                Point<G2D> position = discovery.getSpeciesPos().getPos();
                if (position != null) {
                    dto.setLatitude(position.getPosition().getLat());
                    dto.setLongitude(position.getPosition().getLon());
                }

                return dto;
            })
            .collect(Collectors.toList());
        responseDto.setCollected(collectedDtos);

        return responseDto;
    }
}
