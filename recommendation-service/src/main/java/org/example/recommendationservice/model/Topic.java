package org.example.recommendationservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @ElementCollection
    @CollectionTable(name = "topic_keywords", joinColumns = @JoinColumn(name = "topic_id"))
    @Column(name = "keyword")
    private List<String> keywords;
}
