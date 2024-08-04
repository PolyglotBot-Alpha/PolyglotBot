package org.example.userservice.service;

import org.example.userservice.model.SubscriptionStatus;
import org.example.userservice.model.SubscriptionType;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdateAt(new Timestamp(System.currentTimeMillis()));
        return userRepository.save(user);
    }

    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setUpdateAt(new Timestamp(System.currentTimeMillis()));
        user.setSubscriptionStatus(userDetails.getSubscriptionStatus());
        user.setSubscriptionExpiryDate(userDetails.getSubscriptionExpiryDate());
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Transactional
    public ResponseEntity<?> updateSubscriptionStatus(String userId, SubscriptionType type, String token) {
        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime expiryDate = calculateExpiryDate(type, user.getSubscriptionExpiryDate());

        user.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        user.setSubscriptionExpiryDate(expiryDate);

        // Save user with optimistic locking
        userRepository.save(user);

        // Send message to Subscription-service via Kafka
        String message = createMessage(userId, type, token);
        kafkaTemplate.send("subscription_topic", message);

        return ResponseEntity.ok(user);
    }

    private LocalDateTime calculateExpiryDate(SubscriptionType type, LocalDateTime previous) {
        if (previous == null || previous.isBefore(LocalDateTime.now())) {
            previous = LocalDateTime.now();
        }

        switch (type) {
            case ONE_WEEK:
                return previous.plusWeeks(1);
            case ONE_MONTH:
                return previous.plusMonths(1);
            case ONE_YEAR:
                return previous.plusYears(1);
            default:
                throw new IllegalArgumentException("Unknown subscription type: " + type);
        }
    }

    private String createMessage(String userId, SubscriptionType subscriptionType, String token) {
        return userId + ":" + subscriptionType.name() + ":" + token;
    }
}
