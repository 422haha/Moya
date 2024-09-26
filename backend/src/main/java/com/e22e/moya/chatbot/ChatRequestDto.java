package com.e22e.moya.chatbot;

/**
 * 사용자로부터 받은 메시지를 담는 DTO.
 */
public class ChatRequestDto {
    private String message;

    // Constructor, Getter, Setter
    public ChatRequestDto() {}

    public ChatRequestDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}