package com.e22e.moya.exploration.service.quest;

import static com.e22e.moya.common.entity.quest.QuestStatus.COMPLETE;
import static com.e22e.moya.common.entity.quest.QuestStatus.PROGRESS;
import static com.e22e.moya.common.entity.quest.QuestStatus.WAIT;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.npc.NpcPos;
import com.e22e.moya.common.entity.npc.ParkNpcs;
import com.e22e.moya.common.entity.quest.Quest;
import com.e22e.moya.common.entity.quest.QuestCompleted;
import com.e22e.moya.common.entity.species.ParkSpecies;
import com.e22e.moya.common.entity.species.Species;
import com.e22e.moya.exploration.dto.quest.complete.QuestCompleteResponseDto;
import com.e22e.moya.exploration.dto.quest.list.QuestDto;
import com.e22e.moya.exploration.dto.quest.list.QuestListResponseDto;
import com.e22e.moya.exploration.repository.ExplorationRepositoryExploration;
import com.e22e.moya.exploration.repository.ParkRepositoryExploration;
import com.e22e.moya.exploration.repository.QuestCompletedRepositoryExploration;
import com.e22e.moya.exploration.repository.QuestRepositoryExploration;
import com.e22e.moya.exploration.repository.SpeciesRepositoryExploration;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestServiceImpl implements QuestService {

    private static final Logger log = LoggerFactory.getLogger(QuestServiceImpl.class);
    private final ExplorationRepositoryExploration explorationRepository;
    private final QuestRepositoryExploration questRepository;
    private final QuestCompletedRepositoryExploration questCompletedRepository;
    private final ParkRepositoryExploration parkRepository;
    private final SpeciesRepositoryExploration speciesRepository;
    private final Random random = new Random();

    public QuestServiceImpl(ExplorationRepositoryExploration explorationRepository,
        QuestRepositoryExploration questRepository,
        QuestCompletedRepositoryExploration questCompletedRepository,
        ParkRepositoryExploration parkRepository,
        SpeciesRepositoryExploration speciesRepository) {
        this.explorationRepository = explorationRepository;
        this.questRepository = questRepository;
        this.questCompletedRepository = questCompletedRepository;
        this.parkRepository = parkRepository;
        this.speciesRepository = speciesRepository;
    }

    /**
     * 퀘스트 생성 1
     *
     * @param exploration 탐험
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void generateNewQuests(Exploration exploration) {
        Long parkId = exploration.getPark().getId();
        List<ParkSpecies> parkSpecies = parkRepository.findSpeciesInPark(parkId);
        List<ParkNpcs> parkNpcs = parkRepository.findNpcsInPark(parkId);

        int questSize = Math.min(parkNpcs.size(), 3);
        List<ParkNpcs> selectedNpcs = selectRandomNpcs(parkNpcs, questSize);

        List<ParkSpecies> selectedSpecies = new ArrayList<>();
        if (parkId != 1) {
            selectedSpecies = selectRandomSpecies(parkSpecies, questSize);
        } else {
            selectedSpecies.add(parkSpecies.get(2));
            selectedSpecies.add(parkSpecies.get(2));
            selectedSpecies.add(parkSpecies.get(2));
        }

        for (int i = 0; i < questSize; i++) {
            createQuest(selectedSpecies.get(i), selectedNpcs.get(i), exploration);
        }
    }

    /**
     * 공원 내의 npc 섞어서 뽑아오기
     *
     * @param parkNpcs 공원 내의 npc들
     * @param count    반환할 개수
     */
    private List<ParkNpcs> selectRandomNpcs(List<ParkNpcs> parkNpcs, int count) {
        List<ParkNpcs> selectedNpcs = new ArrayList<>(parkNpcs);
        Collections.shuffle(selectedNpcs);
        return selectedNpcs.subList(0, count);
    }

    /**
     * 공원 내의 species 섞어서 뽑아오기
     *
     * @param parkSpecies 공원 내의 동식물들
     * @param count       반환할 개수
     */
    private List<ParkSpecies> selectRandomSpecies(List<ParkSpecies> parkSpecies, int count) {
        List<ParkSpecies> selectedSpecies = new ArrayList<>();
        int speciesSize = parkSpecies.size();

        if (speciesSize == 0) { // species 없으면 빈 리스트 반환
            return selectedSpecies;
        }

        List<ParkSpecies> shuffledSpecies = new ArrayList<>(parkSpecies);
        Collections.shuffle(shuffledSpecies);

        for (int i = 0; i < count; i++) {
            selectedSpecies.add(shuffledSpecies.get(i % speciesSize));
        }

        return selectedSpecies;
    }

    /**
     * 퀘스트 생성 2
     *
     * @param species     공원에 있는 동식물들
     * @param exploration 탐험
     */
    private void createQuest(ParkSpecies parkSpecies, ParkNpcs parkNpc, Exploration exploration) {
        int questType = 1;

        Species species = parkSpecies.getSpecies();

        List<NpcPos> npcPositions = parkNpc.getPositions();
        NpcPos randomNpcPos = npcPositions.get(random.nextInt(npcPositions.size()));

        Quest quest = new Quest();
        quest.setType(questType);
        quest = questRepository.save(quest);

        QuestCompleted questCompleted = new QuestCompleted();
        questCompleted.setQuest(quest);
        questCompleted.setExploration(exploration);
        questCompleted.setSpeciesId(species.getId());
        questCompleted.setNpcPos(randomNpcPos);
        questCompleted.setStatus(WAIT);
        questCompletedRepository.save(questCompleted);

        log.info("퀘스트 생성: 퀘스트 타입: {}, 동식물 이름: {}, NPC 이름: {}, NPC 위치: {}",
            quest.getType(), species.getName(), parkNpc.getNpc().getName(),
            randomNpcPos.toString());
    }

    /**
     * 퀘스트 반환
     *
     * @param userId        사용자 id
     * @param explorationId 탐험 id
     */
    @Override
    @Transactional(readOnly = true)
    public QuestListResponseDto getQuestList(long userId, Long explorationId) {

        List<QuestCompleted> explorationQuestList = questCompletedRepository.findByExplorationId(
            explorationId);

        if (explorationQuestList.isEmpty()) {
            throw new EntityNotFoundException("도전과제를 찾을 수 없습니다.");
        }

        List<QuestDto> questDTOList = new ArrayList<>();

        for (QuestCompleted questList : explorationQuestList) {
            QuestDto questDTO = new QuestDto();
            questDTO.setQuestId(questList.getId());

            // npc 정보 조회
            NpcPos npcPos = questList.getNpcPos();
            questDTO.setNpcId(npcPos.getParkNpc().getNpc().getId());
            questDTO.setNpcPosId(npcPos.getId());
            questDTO.setNpcName(npcPos.getParkNpc().getNpc().getName());

            // npc 위치 정보 조회
            Point<G2D> point = npcPos.getPos();
            questDTO.setLongitude(point.getPosition().getLon());
            questDTO.setLatitude(point.getPosition().getLat());

            questDTO.setQuestType(questList.getQuest().getType());
            questDTO.setSpeciesId(questList.getSpeciesId());

            // Species 정보 조회
            Species species = speciesRepository.findById(questList.getSpeciesId())
                .orElseThrow(() -> new EntityNotFoundException("동식물을 찾을 수 없습니다."));
            questDTO.setSpeciesName(species.getName());

            questDTO.setCompleted(questList.getStatus().name());

            questDTOList.add(questDTO);
        }

        return new QuestListResponseDto(questDTOList);
    }

    /**
     * 도전과제 완료 처리
     *
     * @param explorationId 탐험 id
     * @param questId       퀘스트 id
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public QuestCompleteResponseDto completeQuest(Long explorationId, Long questId) {
        QuestCompleted questCompleted = questCompletedRepository.findById(questId)
            .orElseThrow(() -> new EntityNotFoundException("도전과제를 찾을 수 없음"));

        questCompleted.setStatus(COMPLETE);
        questCompleted.setCompletedAt(LocalDateTime.now());

        questCompletedRepository.save(questCompleted);

        log.info("도전과제 완료: {}", questId);

        int nOfCompletedQuests = questCompletedRepository.countCompletedQuestsByExplorationId(
            explorationId, COMPLETE);

        return new QuestCompleteResponseDto(questCompleted.getCompletedAt(), nOfCompletedQuests);

    }

    /**
     * 탐험 진행 처리
     *
     * @param questId 퀘스트 id
     */
    @Override
    public void changeStatus(Long questId) {
        QuestCompleted quest = questCompletedRepository.findById(questId)
            .orElseThrow(() -> new EntityNotFoundException("도전과제를 찾을 수 없음"));
        quest.setStatus(PROGRESS);
        questCompletedRepository.save(quest);
        log.info("탐험 진행중으로 상태변경 완료: {}", questId);
    }

}