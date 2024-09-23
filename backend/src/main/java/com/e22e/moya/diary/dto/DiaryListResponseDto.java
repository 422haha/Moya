package com.e22e.moya.diary.dto;

import lombok.AllArgsConstructor;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DiaryListResponseDto {
    private List<DiaryItemDto> explorations;
}
