package com.e22e.moya.chat.service;

import com.e22e.moya.exploration.dto.chat.ChatRequestDto;
import com.e22e.moya.exploration.dto.chat.ChatResponseDto;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public interface ChatService {


//    @Transactional(isolation = Isolation.READ_COMMITTED)
//    ChatResponseDto processUserMessage(ChatRequestDto requestDto, Long npcPosId, Long userId);
}