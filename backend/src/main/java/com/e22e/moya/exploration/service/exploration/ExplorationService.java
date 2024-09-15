package com.e22e.moya.exploration.service.exploration;

import com.e22e.moya.exploration.dto.exploration.AddRequestDto;
import com.e22e.moya.exploration.dto.exploration.AddResponseDto;

public interface ExplorationService {

    AddResponseDto addOnDictionary(long userId, Long explorationId, AddRequestDto addRequestDto);
}
