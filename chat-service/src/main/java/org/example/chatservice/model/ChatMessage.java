package org.example.chatservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

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

    private LocalDateTime time;

    // No-argument constructor
    public ChatMessage() {}

    // All-argument constructor
    public ChatMessage(String userId, String userMessage, String botResponse, LocalDateTime time) {
        this.userId = userId;
        this.userMessage = userMessage;
        this.botResponse = botResponse;
        this.time = time;
    }
}
