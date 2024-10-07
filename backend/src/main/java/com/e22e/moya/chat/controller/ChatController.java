package com.e22e.moya.chat.controller;

import com.e22e.moya.chat.dto.ChatRequestDto;
import com.e22e.moya.chat.dto.ChatResponseDto;
import com.e22e.moya.chat.repository.ExplorationRepositoryChat;
import com.e22e.moya.chat.service.ChatService;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/exploration/{explorationId}/chat")
public class ChatController {

    private final ChatService chatService;
    private final ExplorationRepositoryChat explorationRepository;

    public ChatController(ChatService chatService,
        ExplorationRepositoryChat explorationRepository) {
        this.chatService = chatService;
        this.explorationRepository = explorationRepository;
    }

    @PostMapping("/{npcPosId}")
    public ResponseEntity<Map<String, Object>> doChatting(
        @RequestHeader("Authorization") String token,
        @PathVariable Long explorationId, @PathVariable Long npcPosId,
        @RequestBody ChatRequestDto chatRequestDto) {

        Map<String, Object> response = new HashMap<>();

        try {
//            long userId = jwtUtil.getUserIdFromToken(token);

            long userId = 1;

            Long parkId = explorationRepository.findById(explorationId)
                .orElseThrow(() -> new EntityNotFoundException("탐험 찾을 수 없음"))
                .getPark().getId();

            ChatResponseDto chatResponseDto = chatService.processUserMessage(chatRequestDto,
                npcPosId, userId, parkId);

            response.put("message", "채팅 성공");
            response.put("data", chatResponseDto);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            log.error("엔티티를 찾을 수 없습니다: {}", e.getMessage(), e);
            response.put("message", "엔티티를 찾을 수 없습니다");
            response.put("data", new ChatResponseDto("적절한 대답을 찾지 못했어요."));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            log.error("내부 서버 오류: {}", e.getMessage(), e);
            response.put("message", "내부 서버 오류");
            response.put("data", new ChatResponseDto("잘 못알아들었어요."));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}