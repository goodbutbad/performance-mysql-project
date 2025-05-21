package com.example.demo.repositories;

import com.example.demo.models.Review;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserIdAndPerformance_PerformanceId(Long userId, Long performanceId);


    List<Review> findAllByPerformance_PerformanceId(Long performanceId);


    List<Review> findAllByUserId(Long userId);

    boolean existsByUserIdAndPerformance_PerformanceId(Long userId, Long performanceId);
    @Modifying
    @Transactional
    void deleteByPerformance_PerformanceId(Long performanceId);


}
