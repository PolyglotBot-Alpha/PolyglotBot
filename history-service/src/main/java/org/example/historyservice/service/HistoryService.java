package org.example.historyservice.service;

import org.example.historyservice.model.ChatMessage;
import org.example.historyservice.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @KafkaListener(topics = "chat-messages", groupId = "chat-group")
    public void listen(ChatMessage message) {
        System.out.println("Message"+message.getUserId());
        chatMessageRepository.save(message);
    }

    public List<ChatMessage> getHistoryByUserId(String userId) {
        return chatMessageRepository.findByUserId(userId);
    }

    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAll();
    }

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }
}
