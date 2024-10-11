package com.e22e.moya.collection.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CollectionResponseDto {
    private List<SpeciesDto> species;
    private Double progress;
}
