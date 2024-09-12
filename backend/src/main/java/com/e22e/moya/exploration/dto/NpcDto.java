package com.e22e.moya.exploration.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NpcDto {

    private Long id;
    private String name;
    private List<PositionDto> positions;
}