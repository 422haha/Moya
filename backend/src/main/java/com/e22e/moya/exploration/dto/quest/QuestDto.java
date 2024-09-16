package com.e22e.moya.exploration.dto.quest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestDto {

    private Long id;
    private Long npcId;
    private String npcName;
    private double longitude; // npc의 위치
    private double latitude; // npc의 위치
    private int questType;
    private String speciesName;
}