package org.example.userservice.service;

import org.example.userservice.model.SubscriptionEvent;
import org.example.userservice.model.SubscriptionStatus;
import org.example.userservice.model.User;
import org.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriptionEventListener {
    @Autowired
    private UserService userService;

    @KafkaListener(topics = "subscription-events", groupId = "group_id")
    public void consume(SubscriptionEvent event) {
        Optional<User> userOpt = userService.getUserById(event.getUserId());

        if (userOpt.isPresent() && event.isSuccess()) {
            User user = userOpt.get();
            user.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
            user.setSubscriptionExpiryDate(event.getExpiryDate());
            userService.updateUser(user);
        }
    }
}
