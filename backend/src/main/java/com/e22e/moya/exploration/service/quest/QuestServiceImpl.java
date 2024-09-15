package com.e22e.moya.exploration.service.quest;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.npc.Npc;
import com.e22e.moya.common.entity.npc.NpcPos;
import com.e22e.moya.common.entity.npc.ParkNpcs;
import com.e22e.moya.common.entity.quest.Quest;
import com.e22e.moya.common.entity.quest.QuestCompleted;
import com.e22e.moya.common.entity.species.ParkSpecies;
import com.e22e.moya.common.entity.species.Species;
import com.e22e.moya.exploration.dto.quest.QuestDto;
import com.e22e.moya.exploration.repository.ParkRepository;
import com.e22e.moya.exploration.repository.QuestCompletedRepository;
import com.e22e.moya.exploration.repository.QuestRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private final Random random = new Random();

    public QuestServiceImpl(QuestRepository questRepository,
        QuestCompletedRepository questCompletedRepository, ParkRepository parkRepository) {
        this.questRepository = questRepository;
        this.questCompletedRepository = questCompletedRepository;
        this.parkRepository = parkRepository;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void generateNewQuests(Exploration exploration) {
        Long parkId = exploration.getPark().getId();
        List<ParkSpecies> parkSpecies = parkRepository.findSpeciesInPark(parkId);
        List<ParkNpcs> parkNpcs = parkRepository.findNpcsInPark(parkId);

        List<QuestDto> newQuests = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            QuestDto questDto = createQuest(parkSpecies, parkNpcs, exploration);
            if (questDto != null) {
                newQuests.add(questDto);
            }
        }

    }

    /**
     * 퀘스트 생성
     *
     * @param species     공원에 있는 동식물들
     * @param parkNpcs    공원에 있는 npc들
     * @param exploration 탐험
     */
    private QuestDto createQuest(List<ParkSpecies> species, List<ParkNpcs> parkNpcs,
        Exploration exploration) {

        Quest quest = new Quest();
        quest.setType(random.nextInt(3) + 1);
        quest = questRepository.save(quest);

        ParkSpecies randomParkSpecies = species.get(random.nextInt(species.size()));
        ParkNpcs randomParkNpcs = parkNpcs.get(random.nextInt(parkNpcs.size()));
        Species randomSpecies = randomParkSpecies.getSpecies();
        Npc randomNpcs = randomParkNpcs.getNpc();

        QuestCompleted questCompleted = new QuestCompleted();
        questCompleted.setQuest(quest);
        questCompleted.setExploration(exploration);
        questCompleted.setSpeciesId(randomSpecies.getId());
        questCompleted.setNpcId(randomNpcs.getId());
        questCompleted.setCompleted(false);
        questCompletedRepository.save(questCompleted);

        QuestDto questDto = new QuestDto();
        questDto.setId(quest.getId());
        questDto.setQuestType(quest.getType());
        questDto.setNpcId(randomNpcs.getId());
        questDto.setNpcName(randomNpcs.getName());
        questDto.setSpeciesName(randomSpecies.getName());

        // NPC의 위치 정보 설정
        if (!randomParkNpcs.getPositions().isEmpty()) {
            NpcPos npcPos = randomParkNpcs.getPositions().get(0);
            questDto.setLatitude(npcPos.getPos().getY());
            questDto.setLongitude(npcPos.getPos().getX());
        }

        log.info("퀘스트 생성: 퀘스트 타입: {}, 동식물 이름: {}, NPC 이름: {}",
            quest.getType(), randomSpecies.getName(), randomNpcs.getName());

        return questDto;
    }
}