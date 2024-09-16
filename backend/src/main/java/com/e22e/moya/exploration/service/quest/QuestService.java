package com.e22e.moya.exploration.service.quest;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.exploration.dto.exploration.EndResponseDto;
import com.e22e.moya.exploration.dto.quest.QuestListResponseDto;

public interface QuestService {

    void generateNewQuests(Exploration exploration);

    QuestListResponseDto getQuestList(long userId, Long explorationId);
}
