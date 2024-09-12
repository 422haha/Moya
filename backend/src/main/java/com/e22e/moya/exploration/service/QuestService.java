package com.e22e.moya.exploration.service;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.common.entity.quest.QuestCompleted;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface QuestService {

    @Transactional
    List<QuestCompleted> generateQuestsForExploration(Exploration exploration);
}
