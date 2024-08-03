package org.example.userservice.service;

import org.example.userservice.model.SubscriptionStatus;
import org.example.userservice.model.SubscriptionType;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> register(User user){
        return ResponseEntity.ok(userRepository.save(user));
    }

    public Optional<User> getUserById(String id) {
        return Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public void updateUser(User user) {
        user.setUpdateAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);
    }

    public ResponseEntity<?> updateSubscriptionStatus(String userId, SubscriptionType type) {
        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime expiryDate = calculateExpiryDate(type, user.getSubscriptionExpiryDate());

        user.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        user.setSubscriptionExpiryDate(expiryDate);

        // Save user with optimistic locking
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    private LocalDateTime calculateExpiryDate(SubscriptionType type, LocalDateTime previous) {
        LocalDateTime now = LocalDateTime.now();
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
}
