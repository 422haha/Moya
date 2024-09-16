package com.e22e.moya.exploration.service.quest;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.npc.NpcPos;
import com.e22e.moya.common.entity.npc.ParkNpcs;
import com.e22e.moya.common.entity.quest.Quest;
import com.e22e.moya.common.entity.quest.QuestCompleted;
import com.e22e.moya.common.entity.species.ParkSpecies;
import com.e22e.moya.common.entity.species.Species;
import com.e22e.moya.exploration.dto.quest.QuestDto;
import com.e22e.moya.exploration.dto.quest.QuestListResponseDto;
import com.e22e.moya.exploration.repository.ParkRepository;
import com.e22e.moya.exploration.repository.QuestCompletedRepository;
import com.e22e.moya.exploration.repository.QuestRepository;
import com.e22e.moya.exploration.repository.SpeciesRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
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
    private final QuestRepository questRepository;
    private final QuestCompletedRepository questCompletedRepository;
    private final ParkRepository parkRepository;
    private final SpeciesRepository speciesRepository;
    private final Random random = new Random();

    public QuestServiceImpl(QuestRepository questRepository,
        QuestCompletedRepository questCompletedRepository, ParkRepository parkRepository,
        SpeciesRepository speciesRepository) {
        this.questRepository = questRepository;
        this.questCompletedRepository = questCompletedRepository;
        this.parkRepository = parkRepository;
        this.speciesRepository = speciesRepository;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void generateNewQuests(Exploration exploration) {
        Long parkId = exploration.getPark().getId();
        List<ParkSpecies> parkSpecies = parkRepository.findSpeciesInPark(parkId);
        List<ParkNpcs> parkNpcs = parkRepository.findNpcsInPark(parkId);

        int questSize = Math.min(parkSpecies.size(), 3); // 공원의 npc가 3개 이하면 npc의 개수만큼 퀘스트 생성

        for (int i = 0; i < questSize; i++) {
            createQuest(parkSpecies, parkNpcs, exploration);
        }

    }


    /**
     * 퀘스트 생성
     *
     * @param species     공원에 있는 동식물들
     * @param parkNpcs    공원에 있는 npc들
     * @param exploration 탐험
     */
    private void createQuest(List<ParkSpecies> species, List<ParkNpcs> parkNpcs,
        Exploration exploration) {

        Quest quest = new Quest();
        quest.setType(random.nextInt(3) + 1);
        quest = questRepository.save(quest);

        ParkSpecies randomParkSpecies = species.get(random.nextInt(species.size()));
        ParkNpcs randomParkNpcs = parkNpcs.get(random.nextInt(parkNpcs.size()));
        Species randomSpecies = randomParkSpecies.getSpecies();

        // NPC의 위치 선택
        List<NpcPos> npcPositions = randomParkNpcs.getPositions();
        NpcPos randomNpcPos = npcPositions.get(random.nextInt(npcPositions.size()));

        QuestCompleted questCompleted = new QuestCompleted();
        questCompleted.setQuest(quest);
        questCompleted.setExploration(exploration);
        questCompleted.setSpeciesId(randomSpecies.getId());
        questCompleted.setNpcPos(randomNpcPos);
        questCompleted.setCompleted(false);
        questCompletedRepository.save(questCompleted);

        log.info("퀘스트 생성: 퀘스트 타입: {}, 동식물 이름: {}, NPC 위치: {}",
            quest.getType(), randomSpecies.getName(), randomNpcPos.toString());

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

        List<QuestCompleted> explorationQuestList = questCompletedRepository.findByExplorationUserIdAndExplorationId(userId, explorationId);

        List<QuestDto> questDTOList = new ArrayList<>();

        for (QuestCompleted questList : explorationQuestList) {
            QuestDto questDTO = new QuestDto();
            questDTO.setQuestId(questList.getQuest().getId());

            // npc 정보 조회
            NpcPos npcPos = questList.getNpcPos();
            questDTO.setNpcId(npcPos.getParkNpc().getNpc().getId());
            questDTO.setNpcName(npcPos.getParkNpc().getNpc().getName());

            // npc 위치 정보 조회
            Point<G2D> point = npcPos.getPos();
            questDTO.setLongitude(point.getPosition().getLon());
            questDTO.setLatitude(point.getPosition().getLat());

            questDTO.setQuestType(questList.getQuest().getType());
            questDTO.setSpeciesId(questList.getSpeciesId());

            // Species 정보 조회
            Species species = speciesRepository.findById(questList.getSpeciesId()).orElseThrow(() -> new EntityNotFoundException("Species not found"));
            questDTO.setSpeciesName(species.getName());

            questDTO.setCompleted(questList.isCompleted());

            questDTOList.add(questDTO);
        }

        return new QuestListResponseDto(questDTOList);
    }
}