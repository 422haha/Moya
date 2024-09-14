package com.e22e.moya.exploration.service.info;

import com.e22e.moya.exploration.dto.info.ExplorationStartDto;

public interface InfoService {

    ExplorationStartDto getInitInfo(Long parkId, Long userId);
}
