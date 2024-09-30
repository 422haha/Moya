package com.e22e.moya.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 사용자로부터 받은 메시지를 담는 DTO.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDto {

    private Long npcId; // npc 위치에 대한 id
    private String message;


}