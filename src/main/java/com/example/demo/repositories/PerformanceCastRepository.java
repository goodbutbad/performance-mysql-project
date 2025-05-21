package com.example.demo.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.PerformanceCast;
import org.springframework.transaction.annotation.Transactional;

public interface PerformanceCastRepository extends CrudRepository<PerformanceCast, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO performance_cast (performance_id, actor_id, is_main_character) VALUES (:performanceId, :actorId, 0)", nativeQuery = true)
    void addActorToPerformance(@Param("performanceId") Long performanceId, @Param("actorId") Long actorId);
    void deleteByPerformance_PerformanceId(Integer id);

    

}
