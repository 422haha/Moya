package com.e22e.moya.diary.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class DiaryExplorationDto {

    private Long explorationId;
    private String parkName;
    private LocalDate explorationDate;
    private String imageUrl;
}
