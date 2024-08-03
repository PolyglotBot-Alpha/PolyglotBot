package org.example.subscriptionservice.service;

import org.example.subscriptionservice.model.SubscriptionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubscriptionService {
    @Autowired
    private KafkaTemplate<String, SubscriptionEvent> kafkaTemplate;

    public void processSubscription(String userId) {

    }
}
