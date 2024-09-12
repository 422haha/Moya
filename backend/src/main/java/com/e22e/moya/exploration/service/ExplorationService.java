package com.e22e.moya.exploration.service;

import com.e22e.moya.exploration.dto.ExplorationStartDto;

public interface ExplorationService {

    ExplorationStartDto getInitInfo(Long parkId, Long userId);
}
