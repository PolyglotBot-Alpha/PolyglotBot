package org.example.chatservice.service;

import org.example.chatservice.model.ChatMessage;
import org.example.chatservice.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

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
