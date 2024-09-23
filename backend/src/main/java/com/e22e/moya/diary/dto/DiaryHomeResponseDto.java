package com.e22e.moya.diary.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class DiaryHomeResponseDto {

    private Long parkId;
    private Long explorationId;
    private LocalDate startDate;
    private String parkName;
    private String imageUrl;
}
