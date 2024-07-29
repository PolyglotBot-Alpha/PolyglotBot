package org.example.userservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
public class User {
    @Id
    private String id;

    @Column(unique = false, nullable = false)
    private String username;

    @Column(unique = false, nullable = true)
    private String email;

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    private Timestamp updateAt;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    private LocalDateTime subscriptionExpiryDate;

    @Version
    private Long version;
}

