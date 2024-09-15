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
import com.e22e.moya.exploration.repository.DiscoveryRepository;
import com.e22e.moya.exploration.repository.ExplorationRepository;
import com.e22e.moya.exploration.repository.ParkSpeciesRepository;
import com.e22e.moya.exploration.repository.SpeciesPosRepository;
import com.e22e.moya.exploration.repository.SpeciesRepository;
import com.e22e.moya.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Slf4j
@Service
/*
 *탐험 진행용 서비스.
 * - 탐험중 촬영한 사진 도감에 등록
 * - 탐험 종료 및 기록 저장
 *
 * todo: 탐험 종료 및 기록 저장 구현
 */
public class ExplorationServiceImpl implements ExplorationService {

    private final UserRepository userRepository;
    private final SpeciesRepository speciesRepository;
    private final DiscoveryRepository discoveryRepository;
    private final ExplorationRepository explorationRepository;
    private final ParkSpeciesRepository parkSpeciesRepository;
    private final SpeciesPosRepository speciesPosRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

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
     * @param userId userId
     * @param explorationId 현재 진행중인 exploration의 Id
     * @param addRequestDto 프론트로 응답할 dto
     * */
    @Override
    public AddResponseDto addOnDictionary(long userId, Long explorationId,
        AddRequestDto addRequestDto) {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        Species species = speciesRepository.findById(addRequestDto.getSpeciesId())
            .orElseThrow(() -> new RuntimeException("Species를 찾을 수 없습니다"));

        Exploration exploration = explorationRepository.findById(explorationId)
            .orElseThrow(() -> new RuntimeException("탐험을 찾을 수 없습니다"));

        Park park = exploration.getPark();

        Point point = geometryFactory.createPoint(
            new Coordinate(addRequestDto.getLongitude(), addRequestDto.getLatitude()));

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
                log.info("{}가 새로운 위치에서 발견됨. 위치: {}",parkSpecies.getSpecies().getName(),point);
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
}
