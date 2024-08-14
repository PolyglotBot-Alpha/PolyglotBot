package org.example.chatservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String userId;
}
