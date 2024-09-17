package com.e22e.moya.exploration.service.quest;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.exploration.dto.quest.complete.QuestCompleteResponseDto;
import com.e22e.moya.exploration.dto.quest.list.QuestListResponseDto;

public interface QuestService {

    void generateNewQuests(Exploration exploration);

    QuestListResponseDto getQuestList(long userId, Long explorationId);

    QuestCompleteResponseDto completeQuest(Long explorationId, Long questId);
}
