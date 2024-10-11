package com.e22e.moya.exploration.dto.quest.list;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestListResponseDto {
    private List<QuestDto> quest;

}