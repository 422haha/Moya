package com.e22e.moya.common.entity.chatting;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime messageTime;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean userMessage;

    //getter, setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(LocalDateTime messageTime) {
        this.messageTime = messageTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isUserMessage() {
        return userMessage;
    }

    public void setUserMessage(boolean userMessage) {
        this.userMessage = userMessage;
    }
}
