package com.e22e.moya.exploration.service.info;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.npc.Npc;
import com.e22e.moya.common.entity.npc.NpcPos;
import com.e22e.moya.common.entity.npc.ParkNpcs;
import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.species.SpeciesPos;
import com.e22e.moya.exploration.dto.info.ExplorationStartDto;
import com.e22e.moya.exploration.dto.info.NpcDto;
import com.e22e.moya.exploration.dto.info.ParkSpeciesDto;
import com.e22e.moya.exploration.dto.info.PositionDto;
import com.e22e.moya.exploration.dto.info.SpeciesDto;
import com.e22e.moya.exploration.repository.ExplorationRepository;
import com.e22e.moya.exploration.repository.ParkRepository;
import com.e22e.moya.exploration.service.quest.QuestService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
/*
 * 탐험의 초기정보, 중간 정보 로딩용 서비스
 * - 탐험 초기정보 로드
 * - 진행중 정보 로드
 *
 *todo: 공원의 모든 species 반환: 군집 or 지도의 끝점들 따와서 그리드 쳐서 반환할지...
 */
public class InfoServiceImpl implements InfoService {

    private final ParkRepository parkRepository;
    private final QuestService questService;
    private final ExplorationRepository explorationRepository;

    public InfoServiceImpl(ParkRepository parkRepository, QuestService questService,
        ExplorationRepository explorationRepository) {
        this.parkRepository = parkRepository;
        this.questService = questService;
        this.explorationRepository = explorationRepository;
    }

    /**
     * 초기 탐험 정보 가져오는 메서드
     *
     * @param parkId 공원 id
     * @param userId 사용자 id
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ExplorationStartDto getInitInfo(Long parkId, Long userId) {
        ExplorationStartDto explorationStartDto = new ExplorationStartDto();
        Park park = parkRepository.findById(parkId)
            .orElseThrow(() -> new RuntimeException("공원을 찾을 수 없음"));

        // 내가 공원에서 발견한 것들
        List<ParkSpeciesDto> myDiscoveredSpecies = parkRepository.findMyDiscoveredSpecies(parkId,
            userId);
        List<SpeciesDto> mySpeciesDto = convertToSpeciesDtos(myDiscoveredSpecies);//내가 공원에서 발견한것들

        // 공원에서 발견할 수 있는 것들 군집
//        List<ParkSpeciesDto> allParkSpecies = parkRepository.findAllSpecies(
//            parkId);
//        List<SpeciesDto> allSpeciesDto = convertToSpeciesDtos(allParkSpecies);
        List<NpcDto> npcDtos = getNpcsInPark(park);

        explorationStartDto.setMyDiscoveredSpecies(mySpeciesDto);
//        explorationStartDto.setSpecies(allSpeciesDto);
        explorationStartDto.setNpcs(npcDtos);

        // 새로운 탐험 생성 및 저장
        Exploration exploration = new Exploration();
        exploration.setPark(park);
        exploration.setUserId(userId);
        exploration.setStartDate(LocalDate.now());
        exploration.setStartTime(LocalDateTime.now());
        exploration = explorationRepository.save(exploration);

        questService.generateNewQuests(exploration);
        return explorationStartDto;
    }


    /**
     * 공원에 있는 npc 가져오는 메서드
     *
     * @param park 공원 엔티티
     */
    private List<NpcDto> getNpcsInPark(Park park) {
        List<NpcDto> npcDtoList = new ArrayList<>();
        for (ParkNpcs parkNpc : park.getParkNpcs()) {
            npcDtoList.add(convertToNpcDto(parkNpc));
        }
        return npcDtoList;
    }

    /**
     * ParkSpeciesDto를 SpeciesDto로 변환
     *
     * @param parkSpeciesInfoList 공원의 species 정보 dto list
     */
    private List<SpeciesDto> convertToSpeciesDtos(List<ParkSpeciesDto> parkSpeciesInfoList) {
        Map<Long, SpeciesDto> speciesDtoMap = new HashMap<>();

        for (ParkSpeciesDto info : parkSpeciesInfoList) {
            SpeciesDto speciesDto = speciesDtoMap.computeIfAbsent(info.getSpeciesId(), id -> {
                SpeciesDto dto = new SpeciesDto();
                dto.setId(info.getSpeciesId());
                dto.setName(info.getSpeciesName());
                dto.setScientificName(info.getScientificName());
                dto.setDescription(info.getDescription());
                dto.setImageUrl(info.getImageUrl());
                dto.setPositions(new ArrayList<>());
                return dto;
            });

            PositionDto positionDto = new PositionDto();
            positionDto.setLatitude(info.getPosition().getY());
            positionDto.setLongitude(info.getPosition().getX());
            speciesDto.getPositions().add(positionDto);
        }

        return new ArrayList<>(speciesDtoMap.values());
    }

    /**
     * ParkNpcs 엔티티를 dto로 변환하는 메서드
     *
     * @param parkNpc 엔티티
     */
    private NpcDto convertToNpcDto(ParkNpcs parkNpc) {
        Npc npc = parkNpc.getNpc();
        NpcDto npcDto = new NpcDto();
        npcDto.setId(npc.getId());
        npcDto.setName(npc.getName());
        npcDto.setPositions(convertToPositionDtos(parkNpc.getPositions()));
        return npcDto;
    }

    /**
     * 좌표들을 PositionDto로 변환하는 메서드
     *
     * @param positions 좌표들
     */
    private List<PositionDto> convertToPositionDtos(List<?> positions) {
        List<PositionDto> positionDtoList = new ArrayList<>();
        for (Object pos : positions) {
            PositionDto positionDto = new PositionDto();
            Point point = null;
            if (pos instanceof SpeciesPos) {
                point = ((SpeciesPos) pos).getPos();
            } else if (pos instanceof NpcPos) {
                point = ((NpcPos) pos).getPos();
            }
            if (point != null) {
                positionDto.setLatitude(point.getY());  // PostGIS에서 Y가 위도
                positionDto.setLongitude(point.getX()); // PostGIS에서 X가 경도
            }
            positionDtoList.add(positionDto);
        }
        return positionDtoList;
    }
}