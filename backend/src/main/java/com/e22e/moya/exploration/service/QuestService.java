package com.e22e.moya.exploration.service;

import com.e22e.moya.common.entity.Exploration;
import com.e22e.moya.exploration.dto.QuestDto;
import java.util.List;

public interface QuestService {

    List<QuestDto> generateNewQuests(Exploration exploration);
}
