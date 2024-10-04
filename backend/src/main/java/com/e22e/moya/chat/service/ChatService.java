package com.e22e.moya.chat.service;


import com.e22e.moya.chat.dto.ChatRequestDto;
import com.e22e.moya.chat.dto.ChatResponseDto;

public interface ChatService {

    ChatResponseDto processUserMessage(ChatRequestDto requestDto, Long npcPosId,
        Long userId, Long parkId);
}