package com.e22e.moya.diary.dto;

import lombok.AllArgsConstructor;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LatestDiaryResponseDto {
    private Long parkId;
    private Long explorationId;
    private LocalDate startDate;
    private String parkName;
    private String imageUrl;
    private String imageUrlSmall;
}
