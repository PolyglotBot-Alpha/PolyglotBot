package org.example.historyservice.controller;

import org.example.historyservice.model.ChatMessage;
import org.example.historyservice.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ChatMessage>> getHistoryByUserId(@PathVariable String userId) {
        List<ChatMessage> messages = historyService.getHistoryByUserId(userId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping
    public ResponseEntity<List<ChatMessage>> getAllMessages() {
        List<ChatMessage> messages = historyService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    public ResponseEntity<ChatMessage> saveMessage(@RequestBody ChatMessage message) {
        ChatMessage savedMessage = historyService.saveMessage(message);
        return ResponseEntity.ok(savedMessage);
    }
}
