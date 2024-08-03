package org.example.userservice.model;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private String userId;
    private SubscriptionType subscriptionType;
}
