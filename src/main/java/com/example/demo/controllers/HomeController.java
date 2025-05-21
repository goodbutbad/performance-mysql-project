package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // <-- ПРАВИЛЬНЫЙ import
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Actor;
import com.example.demo.models.Director;
import com.example.demo.models.Performance;
import com.example.demo.models.Review;
import com.example.demo.models.User;
import com.example.demo.repositories.ActorRepository;
import com.example.demo.repositories.DirectorRepository;
import com.example.demo.repositories.PerformanceRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.ReviewRepository;


@Controller
public class HomeController {

    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/home")
    public String home(Model model) {
        List<Performance> performances = performanceRepository.findAll();
        model.addAttribute("performances", performances);
        return "home";
    }
    @GetMapping("/detail/{id}")
public String detail(@PathVariable Long id,
                     Model model,
                     @AuthenticationPrincipal UserDetails userDetails) {
    Performance performance = performanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Спектакль не найден"));
    model.addAttribute("performance", performance);

    

    // 1. Средняя оценка
    List<Review> reviews = reviewRepository.findAllByPerformance_PerformanceId(id);
    model.addAttribute("reviews", reviews);

    double averageRating = reviews.stream()
            .mapToInt(Review::getRating)
            .average()
            .orElse(0.0);
    model.addAttribute("averageRating", averageRating);

    // 2. Проверка: пользователь уже оставлял отзыв?
    boolean alreadyReviewed = false;
    if (userDetails != null) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        alreadyReviewed = reviewRepository.existsByUserIdAndPerformance_PerformanceId(user.getId(), id);
    }
    model.addAttribute("alreadyReviewed", alreadyReviewed);

    return "detail";
}


    @GetMapping("/actor-info/{id}")
public String actorInfo(@PathVariable Integer id, Model model) {
    Actor actor = actorRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid actor ID: " + id));
    model.addAttribute("actor", actor);
    return "actor-info";
}
@GetMapping("/director-info/{id}")
public String directorInfo(@PathVariable Integer id, Model model) {
    Director director = directorRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid director ID: " + id));
    model.addAttribute("director", director);
    return "director-info";
}
@GetMapping("/search")
    public String searchPerformances(@RequestParam(required = false) String query, Model model) {
        List<Performance> results;

        if (query != null && !query.isEmpty()) {
            results = performanceRepository.findByTitleContainingIgnoreCase(query); 
        } else {
            results = List.of(); // или: performanceRepository.findAll();
        }

        model.addAttribute("results", results);
        model.addAttribute("query", query);
        return "search"; 
    }
    
}
