package org.example.chatservice.controller;

import org.example.chatservice.model.ChatMessage;
import org.example.chatservice.model.ChatRequest;
import org.example.chatservice.model.ChatResponse;
import org.example.chatservice.model.UserInput;
import org.example.chatservice.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Autowired
    private KafkaTemplate<String, ChatMessage> kafkaTemplate;

    @Autowired
    private HistoryService historyService;

    @PostMapping("/chat")
    public String chat(@RequestBody UserInput userInput) {
        // create a request
        ChatRequest request = new ChatRequest(model, userInput.getPrompt());
        request.setN(1);

        // call the API
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            logger.debug("No response from the API");
            return "No response";
        }

        historyService.saveMessage(new ChatMessage(userInput.getUserId(), userInput.getPrompt(),
                response.getChoices().get(0).getMessage().getContent(), LocalDateTime.now()));

        // return the first response
        return response.getChoices().get(0).getMessage().getContent();
    }

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
