package com.e22e.moya.diary.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryExplorationDto {

    private Long explorationId;
    private String parkName;
    private LocalDateTime startTime;
    private double distance;
    private int collectedCount;
    private String imageUrl;
    private int duration;
    private int questCompletedCount;
}
