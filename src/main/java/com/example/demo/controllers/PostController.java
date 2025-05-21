package com.example.demo.controllers;

import com.example.demo.models.Actor;
import com.example.demo.models.Director;
import com.example.demo.models.Performance;
import com.example.demo.repositories.ActorRepository;
import com.example.demo.repositories.DirectorRepository;
import com.example.demo.repositories.PerformanceRepository;
import com.example.demo.repositories.PerformanceCastRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;
    private final PerformanceRepository performanceRepository;
    private final PerformanceCastRepository performanceCastRepository;

    public PostController(DirectorRepository directorRepository,
                           ActorRepository actorRepository,
                           PerformanceRepository performanceRepository,
                           PerformanceCastRepository performanceCastRepository) {
        this.directorRepository = directorRepository;
        this.actorRepository = actorRepository;
        this.performanceRepository = performanceRepository;
        this.performanceCastRepository = performanceCastRepository;
    }

    @GetMapping("/post-form")
    public String post(Model model) {
        List<Director> directors = directorRepository.findAll();
        List<Actor> actors = actorRepository.findAll();

        model.addAttribute("directors", directors);
        model.addAttribute("actors", actors);

        return "post";
    }

    @PostMapping("/post-form")
    public String createPerformance(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam("director_id") int directorId,
            @RequestParam("actor_id") List<Long> actorIds,
            @RequestParam int duration,
            @RequestParam String genre,
            @RequestParam("premiere_date") String premiereDate,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                imagePath = image.getOriginalFilename();
                Path filePath = uploadPath.resolve(imagePath);
                image.transferTo(filePath.toFile());
            }

            // Создаем спектакль
            Performance performance = new Performance();
            performance.setTitle(title);
            performance.setDescription(description);
            performance.setDirector(directorRepository.findById(directorId).orElse(null));
            performance.setDuration(duration);
            performance.setGenre(genre);
            performance.setPremiereDate(LocalDate.parse(premiereDate));
            performance.setImagePath(imagePath);

            performanceRepository.save(performance);

            // Связываем актеров (если у тебя есть Entity-связь ManyToMany, здесь можно performance.setActors(actors) )
            if (actorIds != null && !actorIds.isEmpty()) {
                for (Long actorId : actorIds) {
                    performanceRepository.addActorToPerformance(performance.getPerformanceId(), actorId);
                }
            }

            return "redirect:/home";

        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // В случае ошибки показываем страницу ошибки
        }
    }
    
}
