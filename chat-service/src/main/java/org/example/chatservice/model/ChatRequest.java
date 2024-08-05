package org.example.chatservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRequest {
    private String model;
    private List<Message> messages;
    private int n;
    private double temperature;

    public ChatRequest(String model, String prompt) {
        this.model = model;

        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt + "write a short English essay for this (around 50 words)"));

        this.temperature = 1.0;
        this.n = 1;
    }
}
