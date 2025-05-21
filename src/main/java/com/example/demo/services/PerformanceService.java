package com.example.demo.services;

import java.util.List;

import com.example.demo.models.Performance;

public interface PerformanceService {
    Performance save(Performance performance); // Сохранение нового спектакля
    Performance findById(Long id); // Поиск спектакля по ID
    List<Performance> findAll(); 
    void addActorToPerformance(Long performanceId, Long actorId); // Добавить актера в спектакль
    void deleteById(Long id); 
}
