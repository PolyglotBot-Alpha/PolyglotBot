package org.example.subscriptionservice.service;

import org.example.subscriptionservice.model.PaymentStatus;
import org.example.subscriptionservice.model.Subscription;
import org.example.subscriptionservice.model.SubscriptionType;
import org.example.subscriptionservice.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @KafkaListener(topics = "subscription_topic", groupId = "subscription_group")
    public void handleSubscription(String message) {
        // 解析消息内容
        String[] parts = message.split(":");
        String userId = parts[0];
        SubscriptionType subscriptionType = SubscriptionType.valueOf(parts[1]);

        updateSubscriptionRecord(userId, subscriptionType, PaymentStatus.SUCCESSFUL);
    }

    private void updateSubscriptionRecord(String userId, SubscriptionType subscriptionType, PaymentStatus paymentStatus) {
        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setSubscriptionType(subscriptionType);
        subscription.setPaymentStatus(paymentStatus);

        subscriptionRepository.save(subscription);
        System.out.println("Subscription record updated for user: " + userId + " with subscription type: " + subscriptionType);
    }
}
