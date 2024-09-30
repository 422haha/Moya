package com.e22e.moya.exploration.dto.quest.list;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestDto {

    private Long questId;
    private Long npcId;
    private Long npcPosId;
    private double longitude;
    private double latitude;
    private int questType;
    private Long speciesId;
    private String speciesName;
    private String completed;
}
