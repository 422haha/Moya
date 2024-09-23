package com.e22e.moya.diary.service;

import com.e22e.moya.common.entity.Discovery;
import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.diary.dto.*;
import com.e22e.moya.diary.repository.DiaryDiscoveryRepositoryDiary;
import com.e22e.moya.diary.repository.DiaryExplorationRepositoryDiary;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.geolatte.geom.G2D;
import org.geolatte.geom.LineString;
import org.geolatte.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryExplorationRepositoryDiary explorationRepository;
    private final DiaryDiscoveryRepositoryDiary discoveryRepository;

    /**
     * 사용자의 가장 최근 탐험 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return DiaryHomeResponseDto 최근 탐험 정보 DTO
     */
    @Override
    @Transactional(readOnly = true)
    public DiaryHomeResponseDto getLatestExploration(Long userId) {
        // 사용자의 가장 최근 탐험 기록을 조회
        Exploration exploration = explorationRepository.findTopByUserIdOrderByStartTimeDesc(userId);

        // 탐험 기록이 없으면 예외 발생
        if (exploration == null) {
            throw new EntityNotFoundException("최근 탐험을 찾을 수 없습니다.");
        }

        DiaryHomeResponseDto responseDto = new DiaryHomeResponseDto();
        responseDto.setParkId(exploration.getPark().getId());
        responseDto.setExplorationId(exploration.getId());
        responseDto.setStartDate(exploration.getStartDate());
        responseDto.setParkName(exploration.getPark().getName());
        responseDto.setImageUrl(exploration.getPark().getImageUrl());

        return responseDto;
    }

    /**
     * 사용자의 탐험 리스트를 페이지네이션하여 조회합니다.
     *
     * @param userId 사용자 ID
     * @param page   페이지 번호
     * @param size   페이지 크기
     * @return DiaryListResponseDto 탐험 리스트 DTO
     */
    @Override
    @Transactional(readOnly = true)
    public DiaryListResponseDto getExplorations(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        List<Exploration> explorations = explorationRepository
            .findByUserIdOrderByStartTimeDesc(userId, pageRequest)
            .getContent();

        List<DiaryExplorationDto> explorationDtos = explorations.stream()
            .map(exploration -> {
                DiaryExplorationDto dto = new DiaryExplorationDto();
                dto.setExplorationId(exploration.getId());
                dto.setParkName(exploration.getPark().getName());
                dto.setExplorationDate(exploration.getStartDate());
                dto.setImageUrl(exploration.getPark().getImageUrl());
                return dto;
            })
            .collect(Collectors.toList());

        DiaryListResponseDto responseDto = new DiaryListResponseDto();
        responseDto.setExplorations(explorationDtos);

        return responseDto;
    }

    /**
     * 특정 탐험 ID를 기반으로 탐험의 상세 정보를 조회합니다.
     *
     * @param userId        사용자 ID
     * @param explorationId 탐험 ID
     * @return DiaryDetailResponseDto 탐험 상세 정보 DTO
     */
    @Override
    @Transactional(readOnly = true)
    public DiaryDetailResponseDto getExplorationDetail(Long userId, Long explorationId) {
        // userId와 explorationId를 기반으로 탐험을 조회
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

        // User를 통해 Discovery 정보를 조회
        List<Discovery> discoveries = discoveryRepository.findByUserIdAndExplorationId(userId,
            explorationId);
        List<DiaryCollectedDto> collectedDtos = discoveries.stream()
            .map(discovery -> {
                DiaryCollectedDto dto = new DiaryCollectedDto();
                dto.setSpeciesId(discovery.getSpecies().getId());
                dto.setSpeciesName(discovery.getSpecies().getName());
                dto.setImageUrl(discovery.getImageUrl());

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
