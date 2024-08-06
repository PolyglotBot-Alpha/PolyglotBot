package org.example.chatservice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private String userId;
    private String userMessage;
    private String botResponse;
    private LocalDateTime time;

    public ChatMessage(String userId, String userMessage, String botResponse, LocalDateTime time) {
        this.userId = userId;
        this.userMessage = userMessage;
        this.botResponse = botResponse;
        this.time = time;
    }
}
