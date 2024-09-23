package com.e22e.moya.diary.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DiaryListResponseDto {

    private List<DiaryExplorationDto> explorations;
}
