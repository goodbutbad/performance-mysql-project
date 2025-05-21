package com.example.demo.controllers;

import com.example.demo.models.Performance;
import com.example.demo.models.Review;
import com.example.demo.models.User;
import com.example.demo.repositories.PerformanceRepository;
import com.example.demo.repositories.ReviewRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.ui.Model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    @GetMapping("/review/{id}")
public String showReviewForm(@PathVariable Long id, Model model) {
    Performance performance = performanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Спектакль не найден"));

    model.addAttribute("performance", performance);
    model.addAttribute("review", new Review()); // для формы

    return "review"; 
}

    @PostMapping("/performances/{performanceId}/reviews")
    public String submitReview(@PathVariable Long performanceId,
                               @ModelAttribute Review review,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        boolean alreadyReviewed = reviewRepository.existsByUserIdAndPerformance_PerformanceId(user.getId(), performanceId);
        if (alreadyReviewed) {
            redirectAttributes.addFlashAttribute("error", "Вы уже оставили отзыв на этот спектакль.");
            return "redirect:/performances/" + performanceId;
        }

        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new RuntimeException("Спектакль не найден"));

        review.setUser(user);
        review.setReviewerName(user.getUsername());
        review.setPerformance(performance);
        reviewRepository.save(review);

        return "redirect:/detail/" + performanceId;
    }
    @GetMapping("/show-reviews/{performanceId}")
public String showReviews(@PathVariable Long performanceId, Model model) {
    Performance performance = performanceRepository.findById(performanceId)
            .orElseThrow(() -> new RuntimeException("Спектакль не найден"));
    User user = userRepository.findByUsername("admin").orElse(null);

    List<Review> reviews = reviewRepository.findAllByPerformance_PerformanceId(performanceId);

    model.addAttribute("performance", performance);
    model.addAttribute("user", user);
    model.addAttribute("reviews", reviews);

    return "show-reviews"; 
}

}
