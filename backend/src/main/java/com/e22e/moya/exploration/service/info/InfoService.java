package com.e22e.moya.exploration.service.info;

import com.e22e.moya.exploration.dto.info.ExplorationInfoDto;

public interface InfoService {

    ExplorationInfoDto getInitInfo(Long parkId, Long userId);

    ExplorationInfoDto getInfo(Long explorationId, long userId);
}
