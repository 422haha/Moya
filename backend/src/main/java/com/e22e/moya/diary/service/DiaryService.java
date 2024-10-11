package com.e22e.moya.diary.service;

import com.e22e.moya.diary.dto.DiaryHomeResponseDto;
import com.e22e.moya.diary.dto.DiaryListResponseDto;
import com.e22e.moya.diary.dto.DiaryDetailResponseDto;

public interface DiaryService {

    DiaryHomeResponseDto getLatestExploration(Long userId);

    DiaryListResponseDto getExplorations(Long userId, int page, int size);

    DiaryDetailResponseDto getExplorationDetail(Long userId, Long explorationId);
}
