package com.e22e.moya.exploration.service;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.npc.NpcPos;
import com.e22e.moya.common.entity.npc.ParkNpcs;
import com.e22e.moya.common.entity.quest.Quest;
import com.e22e.moya.common.entity.quest.QuestCompleted;
import com.e22e.moya.common.entity.species.Species;
import com.e22e.moya.exploration.repository.ParkRepository;
import com.e22e.moya.exploration.repository.QuestCompletedRepository;
import com.e22e.moya.exploration.repository.QuestRepository;
import org.locationtech.jts.geom.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestServiceImpl implements QuestService {

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
    @Transactional
    public List<QuestCompleted> generateQuestsForExploration(Exploration exploration) {
        // 공원에 있는 동식물들
        List<Species> parkSpecies = parkRepository.findSpeciesInPark(exploration.getPark().getId());

        // 공원에 있는 npc들
        List<ParkNpcs> parkNpcs = exploration.getPark().getParkNpcs();

        List<QuestCompleted> newQuests = new ArrayList<>();

        for (int i = 0; i < 3; i++) {  // 3개의 퀘스트 생성
            QuestCompleted questCompleted = createQuest(parkSpecies, parkNpcs, exploration);
            if (questCompleted != null) {
                newQuests.add(questCompleted);
            }
        }

        return questCompletedRepository.saveAll(newQuests);
    }

    /**
     * 퀘스트 생성
     *
     * @param species     공원에 있는 동식물들
     * @param parkNpcs    공원에 있는 npc들
     * @param exploration 탐험
     */
    private QuestCompleted createQuest(List<Species> species, List<ParkNpcs> parkNpcs,
        Exploration exploration) {
        return null;
    }
}