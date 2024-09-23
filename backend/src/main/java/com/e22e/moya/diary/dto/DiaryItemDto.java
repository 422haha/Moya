package com.e22e.moya.diary.dto;

import lombok.AllArgsConstructor;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DiaryItemDto {
    private Long explorationId;
    private String parkName;
    private LocalDate explorationDate;
    private String imageUrl;
    private String imageUrlSmall;
}
