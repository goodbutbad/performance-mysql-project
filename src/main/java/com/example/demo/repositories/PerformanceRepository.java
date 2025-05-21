package com.example.demo.repositories;

import com.example.demo.models.Performance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO performance_cast (performance_id, actor_id, role, is_main_character) VALUES (?1, ?2, '', false)", nativeQuery = true)
    void addActorToPerformance(Long performanceId, Long actorId);
    
    @Transactional
@Modifying
@Query("DELETE FROM PerformanceCast pc WHERE pc.performance.id = :performanceId")
void deleteAllActorsByPerformanceId(@Param("performanceId") Long performanceId);
List<Performance> findByTitleContainingIgnoreCase(String title);



   

}
