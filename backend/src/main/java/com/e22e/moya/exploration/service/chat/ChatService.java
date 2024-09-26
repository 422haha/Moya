package com.e22e.moya.chatbot;

public interface ChatService {
    /**
     * 사용자 메시지 처리, AI 응답 생성, DB 저장
     *
     * @param requestDto 사용자 메시지 DTO
     * @param chatId 채팅 ID, 이전 대화 내역을 불러오기 위해 사용
     * @return ChatResponseDto 객체
     */
    ChatResponseDto processUserMessage(ChatRequestDto requestDto, Long chatId);
}