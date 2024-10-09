package com.e22e.moya.chat.service;


import com.e22e.moya.chat.dto.ChatRequestDto;
import com.e22e.moya.chat.dto.ChatResponseDto;
import com.e22e.moya.common.entity.species.Species;

public interface ChatService {

    ChatResponseDto processUserMessage(ChatRequestDto requestDto, Long npcPosId,
        Long userId, Long parkId, Long explorationId);

    void updateSpeciesCache(Long parkId, Species newSpecies);
}