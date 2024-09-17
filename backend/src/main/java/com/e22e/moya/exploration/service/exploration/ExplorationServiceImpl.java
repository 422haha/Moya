package com.e22e.moya.exploration.service.exploration;

import com.e22e.moya.common.entity.Discovery;
import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.Users;
import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.species.ParkSpecies;
import com.e22e.moya.common.entity.species.Species;
import com.e22e.moya.common.entity.species.SpeciesPos;
import com.e22e.moya.exploration.dto.exploration.AddRequestDto;
import com.e22e.moya.exploration.dto.exploration.AddResponseDto;
import com.e22e.moya.exploration.dto.exploration.EndRequestDto;
import com.e22e.moya.exploration.dto.exploration.EndResponseDto;
import com.e22e.moya.exploration.repository.DiscoveryRepository;
import com.e22e.moya.exploration.repository.ExplorationRepository;
import com.e22e.moya.exploration.repository.ParkSpeciesRepository;
import com.e22e.moya.exploration.repository.SpeciesPosRepository;
import com.e22e.moya.exploration.repository.SpeciesRepository;
import com.e22e.moya.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.G2D;
import org.geolatte.geom.LineString;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
/*
 *탐험 진행용 서비스.
 * - 탐험중 촬영한 사진 도감에 등록
 * - 탐험 종료 및 기록 저장
 *
 */
public class ExplorationServiceImpl implements ExplorationService {

    private final UserRepository userRepository;
    private final SpeciesRepository speciesRepository;
    private final DiscoveryRepository discoveryRepository;
    private final ExplorationRepository explorationRepository;
    private final ParkSpeciesRepository parkSpeciesRepository;
    private final SpeciesPosRepository speciesPosRepository;

    public ExplorationServiceImpl(UserRepository userRepository,
        SpeciesRepository speciesRepository, DiscoveryRepository discoveryRepository,
        ExplorationRepository explorationRepository, ParkSpeciesRepository parkSpeciesRepository,
        SpeciesPosRepository speciesPosRepository) {
        this.userRepository = userRepository;
        this.speciesRepository = speciesRepository;
        this.discoveryRepository = discoveryRepository;
        this.explorationRepository = explorationRepository;
        this.parkSpeciesRepository = parkSpeciesRepository;
        this.speciesPosRepository = speciesPosRepository;
    }

    /**
     * 탐험중 촬영한 사진 도감에 등록하는 메서드
     *
     * @param userId        userId
     * @param explorationId 현재 진행중인 exploration의 Id
     * @param addRequestDto 프론트로 응답할 dto
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public AddResponseDto addOnDictionary(long userId, Long explorationId,
        AddRequestDto addRequestDto) {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));

        Species species = speciesRepository.findById(addRequestDto.getSpeciesId())
            .orElseThrow(() -> new EntityNotFoundException("Species를 찾을 수 없습니다"));

        Exploration exploration = explorationRepository.findById(explorationId)
            .orElseThrow(() -> new EntityNotFoundException("탐험을 찾을 수 없습니다"));

        Park park = exploration.getPark();

        Point<G2D> point = DSL.point(CoordinateReferenceSystems.WGS84,
            DSL.g(addRequestDto.getLongitude(), addRequestDto.getLatitude()));

        // 공원에 있는 종인지 확인
        ParkSpecies parkSpecies = parkSpeciesRepository.findByParkAndSpecies(park, species)
            .orElseGet(() -> {
                // 공원에 없다면 새로운 종으로 등록
                ParkSpecies newParkSpecies = new ParkSpecies();
                newParkSpecies.setPark(park);
                newParkSpecies.setSpecies(species);
                return parkSpeciesRepository.save(newParkSpecies);
            });

        // 새로운 위치에서 발견
        SpeciesPos speciesPos = speciesPosRepository.findByParkSpeciesAndPos(parkSpecies, point)
            .orElseGet(() -> {
                // 새로운 위치에서 발견되었다면 위치 등록
                log.info("{}가 새로운 위치에서 발견됨. 위치: {}", parkSpecies.getSpecies().getName(), point);
                SpeciesPos newPos = new SpeciesPos();
                newPos.setParkSpecies(parkSpecies);
                newPos.setPos(point);
                return speciesPosRepository.save(newPos);
            });

        // discovery 생성
        Discovery discovery = new Discovery();
        discovery.setUserId(userId);
        discovery.setSpecies(species);
        discovery.setSpeciesPos(speciesPos);
        discovery.setImageUrl(addRequestDto.getImageUrl());
        discovery.setDiscoveryTime(LocalDateTime.now());
        discovery = discoveryRepository.save(discovery);

        // 사용자의 discoveries에 추가
        user.getDiscoveries().add(discovery);
        userRepository.save(user);

        // 현재 탐험에 discovery 추가
        exploration.getDiscoveries().add(discovery);
        explorationRepository.save(exploration);

        AddResponseDto responseDto = new AddResponseDto();
        responseDto.setSpeciesId(species.getId());
        responseDto.setSpeciesName(species.getName());

        return responseDto;
    }

    /**
     * 탐험 종료 처리
     *
     * @param userId        userId
     * @param explorationId 현재 진행중인 exploration의 Id
     * @param endRequestDto 탐험 종료 request dto. 이동 경로 및 걸음 수 들어있음
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public EndResponseDto endExploration(long userId, Long explorationId,
        EndRequestDto endRequestDto) {
        Exploration exploration = explorationRepository.findById(explorationId)
            .orElseThrow(() -> new EntityNotFoundException("탐험을 찾을 수 없습니다"));

        // 사용자 id 확인
        if (exploration.getUserId() != userId) {
            throw new IllegalArgumentException("권한이 없습니다");
        }

        // route를 lineString으로
        List<G2D> positionList = new ArrayList<>();
        for (EndRequestDto.Points point : endRequestDto.getRoute()) {
            if (point.isValid()) {
                G2D position = DSL.g(point.getLongitude(), point.getLatitude());
                positionList.add(position);
            }
        }
        G2D[] positions = positionList.toArray(new G2D[0]);

        LineString<G2D> lineString = DSL.linestring(CoordinateReferenceSystems.WGS84, positions);

        exploration.setRoute(lineString);
        exploration.setSteps(endRequestDto.getSteps());
        exploration.setEndTime(LocalDateTime.now());
        explorationRepository.save(exploration); // 기존 정보 저장e

        // 저장된 linestring을 바탕으로 PostGIS 사용하여 거리 계산 및 저장
        Double distance = explorationRepository.calculateDistance(exploration.getId());

        if (distance != null) {
            exploration.setDistance(distance);
        } else {
            exploration.setDistance(0);
        }
        exploration = explorationRepository.save(exploration);

        EndResponseDto responseDto = new EndResponseDto();
        responseDto.setExplorationId(exploration.getId());

        return responseDto;
    }
}
