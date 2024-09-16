package com.e22e.moya.exploration.service.exploration;

import com.e22e.moya.exploration.dto.exploration.AddRequestDto;
import com.e22e.moya.exploration.dto.exploration.AddResponseDto;
import com.e22e.moya.exploration.dto.exploration.EndRequestDto;
import com.e22e.moya.exploration.dto.exploration.EndResponseDto;

public interface ExplorationService {

    AddResponseDto addOnDictionary(long userId, Long explorationId, AddRequestDto addRequestDto);

    EndResponseDto endExploration(long userId, Long explorationId, EndRequestDto endRequestDto);
}
