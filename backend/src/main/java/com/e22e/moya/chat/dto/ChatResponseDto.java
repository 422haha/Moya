package com.e22e.moya.chat.dto;

/**
 * 챗봇의 응답을 담는 DTO.
 */
public class ChatResponseDto {
    private String response; // 챗봇의 응답 메시지

    // Constructor, Getter, Setter
    public ChatResponseDto() {}

    public ChatResponseDto(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}