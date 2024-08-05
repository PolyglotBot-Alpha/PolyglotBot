package org.example.chatservice.model;

import lombok.Data;

@Data
public class ChatMessage {
    private String userId;
    private String userMessage;
    private String botResponse;

    public ChatMessage(String userId, String userMessage, String botResponse) {
        this.userId = userId;
        this.userMessage = userMessage;
        this.botResponse = botResponse;
    }
}
