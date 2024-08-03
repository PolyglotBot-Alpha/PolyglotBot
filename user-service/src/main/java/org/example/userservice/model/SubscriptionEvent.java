package org.example.userservice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionEvent {
    private String userId;
    private boolean success;
    private String message;
    private LocalDateTime expiryDate;
    private SubscriptionType subscriptionType;
}
