package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String reviewerName;
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private LocalDateTime reviewDate = LocalDateTime.now();
}
