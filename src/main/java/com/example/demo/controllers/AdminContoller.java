package com.example.demo.controllers;

import com.example.demo.models.Actor;
import com.example.demo.models.Director;
import com.example.demo.models.Performance;
import com.example.demo.repositories.ActorRepository;
import com.example.demo.repositories.DirectorRepository;
import com.example.demo.repositories.PerformanceRepository;
import com.example.demo.repositories.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
public class AdminContoller {

    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Performance> performances = performanceRepository.findAll();
        model.addAttribute("performances", performances);
        return "dashboard";
    }
      
    @PostMapping("/delete/{id}")
    public String deletePerformance(@PathVariable("id") Integer id) {
        Long performanceId = Long.valueOf(id);
    
        Performance performance = performanceRepository.findById(performanceId).orElse(null);
    
        if (performance != null) {
            // Удалить отзывы, связанные со спектаклем
            reviewRepository.deleteByPerformance_PerformanceId(performanceId);
    
            // Удалить изображение (если есть)
            if (performance.getImagePath() != null && !performance.getImagePath().isEmpty()) {
                try {
                    Path imagePath = Paths.get("uploads").resolve(performance.getImagePath());
                    Files.deleteIfExists(imagePath);
                } catch (Exception e) {
                    e.printStackTrace(); // логирование
                }
            }
    
            // Удалить сам спектакль
            performanceRepository.deleteById(performanceId);
        }
    
        return "redirect:/dashboard";
    }
    


  @GetMapping("/edit/{id}")
public String editPerformance(@PathVariable("id") Integer id, Model model) {
    Performance performance = performanceRepository.findById(Long.valueOf(id)).orElse(null);
    List<Director> directors = directorRepository.findAll();
    List<Actor> actors = actorRepository.findAll();

    model.addAttribute("performance", performance);
    model.addAttribute("directors", directors);
    model.addAttribute("actors", actors);

    return "edit";
}
@PostMapping("/update")
public String updatePerformance(
        @RequestParam("performanceId") Long performanceId,
        @RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestParam("director_id") Integer directorId,
        @RequestParam(value = "actor_id", required = false) List<Long> actorIds,
        @RequestParam("duration") Integer duration,
        @RequestParam("genre") String genre,
        @RequestParam("premiere_date") String premiereDate,
        @RequestParam(value = "image", required = false) MultipartFile imageFile
) {
    Performance performance = performanceRepository.findById(Long.valueOf(performanceId)).orElse(null); // ✅ правильно

    if (performance == null) return "redirect:/dashboard";

    performance.setTitle(title);
    performance.setDescription(description);
    performance.setDuration(duration);
    performance.setGenre(genre);
    performance.setPremiereDate(LocalDate.parse(premiereDate));
    performance.setDirector(directorRepository.findById(directorId).orElse(null));

    // Обновление изображения
   if (imageFile != null && !imageFile.isEmpty()) {
    try {
        String uploadDirPath = System.getProperty("user.dir") + "/uploads/";
        Path uploadDir = Paths.get(uploadDirPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Удаляем старое изображение, если оно есть
        if (performance.getImagePath() != null && !performance.getImagePath().isBlank()) {
            Path oldFile = uploadDir.resolve(performance.getImagePath());
            Files.deleteIfExists(oldFile);
        }

        // Генерируем уникальное имя для нового файла
        String newFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path newFilePath = uploadDir.resolve(newFileName);
        
        // Сохраняем файл
        imageFile.transferTo(newFilePath.toFile());

        // Обновляем путь к изображению в базе данных
        performance.setImagePath(newFileName);
    } catch (Exception e) {
        e.printStackTrace();  // Лучше логировать, если приложение в production
    }
}

    performanceRepository.save(performance);

    // Обновляем актеров (удаляем всех и добавляем заново)
    if (actorIds != null) {
        performanceRepository.deleteAllActorsByPerformanceId(performanceId); // Удаляем старые связи
    
        for (Long actorId : actorIds) {
            performanceRepository.addActorToPerformance(performanceId, actorId);
        }
    }

    return "redirect:/dashboard";
}

    
    
}
