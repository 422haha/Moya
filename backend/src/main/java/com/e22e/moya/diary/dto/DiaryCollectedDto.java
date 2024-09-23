package com.e22e.moya.diary.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryCollectedDto {

    private Long speciesId;
    private String speciesName;
    private String imageUrl;
    private double latitude;
    private double longitude;
}
