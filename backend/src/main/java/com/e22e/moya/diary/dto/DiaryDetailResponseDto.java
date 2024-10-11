package com.e22e.moya.diary.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DiaryDetailResponseDto {

    private Long explorationId;
    private String parkName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double distance;
    private int steps;
    private List<DiaryRouteDto> route;
    private List<DiaryCollectedDto> collected;
}
