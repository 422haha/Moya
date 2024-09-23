package com.e22e.moya.diary.dto;

import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DiaryDetailResponseDto {
    private Long explorationId;
    private String parkName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double distance;
    private int steps;
    private List<CollectedSpeciesDto> collectedSpecies;
    private List<String> movementPath;
}
