package com.e22e.moya.exploration.service.info;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.npc.Npc;
import com.e22e.moya.common.entity.npc.NpcPos;
import com.e22e.moya.common.entity.npc.ParkNpcs;
import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.quest.QuestStatus;
import com.e22e.moya.common.s3Service.PresignedUrlService;
import com.e22e.moya.exploration.dto.info.ExplorationInfoDto;
import com.e22e.moya.exploration.dto.info.NpcDto;
import com.e22e.moya.exploration.repository.ParkSpeciesProjection;
import com.e22e.moya.exploration.dto.info.PositionDto;
import com.e22e.moya.exploration.dto.info.SpeciesDto;
import com.e22e.moya.exploration.repository.ExplorationRepositoryExploration;
import com.e22e.moya.exploration.repository.ParkRepositoryExploration;
import com.e22e.moya.exploration.repository.QuestCompletedRepositoryExploration;
import com.e22e.moya.exploration.service.quest.QuestService;
import com.e22e.moya.park.service.PopularParkService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.*;
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
 */
public class InfoServiceImpl implements InfoService {

    private final ParkRepositoryExploration parkRepository;
    private final QuestService questService;
    private final ExplorationRepositoryExploration explorationRepository;
    private final QuestCompletedRepositoryExploration questCompletedRepository;
    private final PopularParkService popularParkService;
    private final PresignedUrlService presignedUrlService;

    public InfoServiceImpl(ParkRepositoryExploration parkRepository, QuestService questService,
        ExplorationRepositoryExploration explorationRepository,
        QuestCompletedRepositoryExploration questCompletedRepository,
        PopularParkService popularParkService, PresignedUrlService presignedUrlService) {
        this.parkRepository = parkRepository;
        this.questService = questService;
        this.explorationRepository = explorationRepository;
        this.questCompletedRepository = questCompletedRepository;
        this.popularParkService = popularParkService;
        this.presignedUrlService = presignedUrlService;
    }

    /**
     * 새 탐험 시작 초기 탐험 정보 가져오는 메서드
     *
     * @param parkId 공원 id
     * @param userId 사용자 id
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ExplorationInfoDto getInitInfo(Long parkId, Long userId) {
        ExplorationInfoDto explorationStartDto = new ExplorationInfoDto();
        Park park = parkRepository.findById(parkId)
            .orElseThrow(() -> new EntityNotFoundException("공원을 찾을 수 없음"));

        // 내가 공원에서 발견한 것들
        List<ParkSpeciesProjection> myDiscoveredSpecies = parkRepository.findMyDiscoveredSpecies(
            parkId,
            userId);
        List<SpeciesDto> mySpeciesDto = convertToSpeciesDtos(myDiscoveredSpecies);//내가 공원에서 발견한것들

        // 공원에서 발견할 수 있는 것들 군집
        List<ParkSpeciesProjection> allParkSpecies = parkRepository.findAllSpecies(
            parkId);

        // 내가 발견한 것 위치는 제외하도록
        List<ParkSpeciesProjection> filteredParkSpecies = filterDiscoveredSpecies(allParkSpecies,
            myDiscoveredSpecies);

        List<SpeciesDto> allSpeciesDto = convertToSpeciesDtos(filteredParkSpecies);
        List<NpcDto> npcDtos = getNpcsInPark(park);

        explorationStartDto.setMyDiscoveredSpecies(mySpeciesDto);
        explorationStartDto.setSpecies(allSpeciesDto);
        explorationStartDto.setNpcs(npcDtos);

        // 새로운 탐험 생성 및 저장
        Exploration exploration = new Exploration();
        exploration.setPark(park);
        exploration.setUserId(userId);
        exploration.setStartDate(LocalDate.now());
        exploration.setStartTime(LocalDateTime.now());
        exploration = explorationRepository.save(exploration);

        explorationStartDto.setExplorationId(exploration.getId());
        questService.generateNewQuests(exploration);

        popularParkService.increaseParkPopularity(parkId);
        log.info("탐험 초기정보 생성 완료. 탐험 id: {}", explorationStartDto.getExplorationId());
        return explorationStartDto;
    }

    /**
     * 탐험 정보 로드 메서드
     *
     * @param explorationId 탐험 id
     * @param userId        사용자 id
     * @param parkId        공원 id
     */
    @Transactional(readOnly = true)
    public ExplorationInfoDto getInfo(Long parkId, Long explorationId, long userId) {

        ExplorationInfoDto infoLoadDto = new ExplorationInfoDto();
        Park park = parkRepository.findById(parkId)
            .orElseThrow(() -> new EntityNotFoundException("공원을 찾을 수 없음"));

        // 내가 공원에서 발견한 것들
        List<ParkSpeciesProjection> myDiscoveredSpecies = parkRepository.findMyDiscoveredSpecies(
            parkId,
            userId);
        List<SpeciesDto> mySpeciesDto = convertToSpeciesDtos(myDiscoveredSpecies);//내가 공원에서 발견한것들

        // 공원에서 발견할 수 있는 것들 군집
        List<ParkSpeciesProjection> allParkSpecies = parkRepository.findAllSpecies(
            parkId);

        // 내가 발견한 것 위치는 제외하도록
        List<ParkSpeciesProjection> filteredParkSpecies = filterDiscoveredSpecies(allParkSpecies,
            myDiscoveredSpecies);

        List<SpeciesDto> allSpeciesDto = convertToSpeciesDtos(filteredParkSpecies);
        List<NpcDto> npcDtos = getNpcsInPark(park);

        infoLoadDto.setMyDiscoveredSpecies(mySpeciesDto);
        infoLoadDto.setSpecies(allSpeciesDto);
        infoLoadDto.setNpcs(npcDtos);

        infoLoadDto.setExplorationId(explorationId);
        infoLoadDto.setCompletedQuests(
            questCompletedRepository.countCompletedQuestsByExplorationId(explorationId,
                QuestStatus.COMPLETE));
        return infoLoadDto;
    }

    /**
     * 내가 발견한 종의 위치와 내가 발견한 종의 모든 위치가 겹치지 않도록
     */
    private List<ParkSpeciesProjection> filterDiscoveredSpecies(
        List<ParkSpeciesProjection> allSpecies,
        List<ParkSpeciesProjection> discoveredSpecies) {
        List<ParkSpeciesProjection> filteredSpecies = new ArrayList<>();
        Map<Point<G2D>, Set<Long>> speciesAtPos = new HashMap<>();

        // 발견된 종의 위치와 종 id를 map에 추가
        for (ParkSpeciesProjection discovered : discoveredSpecies) {
            Point<G2D> position = discovered.getPosition();
            Long speciesId = discovered.getSpeciesId();
            if (!speciesAtPos.containsKey(position)) {
                speciesAtPos.put(position, new HashSet<>());
            }
            speciesAtPos.get(position).add(speciesId);
        }

        // allSpecies에서 필터링
        for (ParkSpeciesProjection species : allSpecies) {
            Point<G2D> position = species.getPosition();
            Long speciesId = species.getSpeciesId();
            if (!speciesAtPos.containsKey(position) ||
                !speciesAtPos.get(position).contains(speciesId)) {
                filteredSpecies.add(species);
            }
        }

        return filteredSpecies;
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
    private List<SpeciesDto> convertToSpeciesDtos(List<ParkSpeciesProjection> parkSpeciesInfoList) {
        Map<Long, SpeciesDto> speciesDtoMap = new HashMap<>();

        for (ParkSpeciesProjection info : parkSpeciesInfoList) {
            Long speciesId = info.getSpeciesId();
            SpeciesDto speciesDto;

            if (speciesDtoMap.containsKey(speciesId)) {
                speciesDto = speciesDtoMap.get(speciesId);
            } else {
                speciesDto = new SpeciesDto();
                speciesDto.setId(info.getSpeciesId());
                speciesDto.setName(info.getSpeciesName());
                speciesDto.setScientificName(info.getScientificName());
                speciesDto.setDescription(info.getDescription());
                speciesDto.setImageUrl(presignedUrlService.getPresignedUrl(info.getImageUrl()));
                speciesDto.setPositions(new ArrayList<>());
                speciesDtoMap.put(speciesId, speciesDto);
            }

            PositionDto positionDto = new PositionDto();
            Point<G2D> point = info.getPosition();
            positionDto.setLatitude(point.getPosition().getLat());
            positionDto.setLongitude(point.getPosition().getLon());
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
    private List<PositionDto> convertToPositionDtos(List<NpcPos> positions) {
        List<PositionDto> positionDtoList = new ArrayList<>();
        for (NpcPos pos : positions) {
            PositionDto positionDto = new PositionDto();
            Point<G2D> point = pos.getPos();
            if (point != null) {
                positionDto.setId(pos.getId());
                positionDto.setLatitude(point.getPosition().getLat());
                positionDto.setLongitude(point.getPosition().getLon());
            }
            positionDtoList.add(positionDto);
        }
        return positionDtoList;
    }
}