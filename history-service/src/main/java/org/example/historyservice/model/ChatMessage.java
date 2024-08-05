package org.example.historyservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String userMessage;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String botResponse;

    // No-argument constructor
    public ChatMessage() {}

    // All-argument constructor
    public ChatMessage(String userId, String userMessage, String botResponse) {
        this.userId = userId;
        this.userMessage = userMessage;
        this.botResponse = botResponse;
    }
}
