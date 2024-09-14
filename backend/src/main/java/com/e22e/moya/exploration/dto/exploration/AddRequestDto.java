package com.e22e.moya.exploration.dto.exploration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddRequestDto {

    private Long speciesId;
    private String imageUrl;
    private double latitude;
    private double longitude;
}
