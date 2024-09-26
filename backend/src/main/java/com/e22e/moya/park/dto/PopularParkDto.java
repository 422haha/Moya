package com.e22e.moya.park.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PopularParkDto {

    private Long id;
    private String name;
    private String imageUrl;
    private Double distance;
    private Double popularity;

}
