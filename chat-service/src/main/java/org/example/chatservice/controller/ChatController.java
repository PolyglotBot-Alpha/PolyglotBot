package org.example.chatservice.controller;

import org.example.chatservice.model.ChatRequest;
import org.example.chatservice.model.ChatResponse;
import org.example.chatservice.model.UserInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @PostMapping("/chat")
    public String chat(@RequestBody UserInput userInput) {
        // create a request
        ChatRequest request = new ChatRequest(model, userInput.getPrompt());
        request.setN(1);

        // Log the request payload
        logger.debug("Request Payload: {}", request);

        // call the API
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            logger.debug("No response from the API");
            return "No response";
        }

        // return the first response
        return response.getChoices().get(0).getMessage().getContent();
    }

}
