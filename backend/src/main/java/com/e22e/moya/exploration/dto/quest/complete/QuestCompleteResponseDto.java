package com.e22e.moya.exploration.dto.quest.complete;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestCompleteResponseDto {

    private LocalDateTime completionDate;
    private int completedQuests;

}
