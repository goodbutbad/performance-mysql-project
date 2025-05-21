package com.example.demo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Actor;
import com.example.demo.repositories.ActorRepository;

import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;



@Controller

public class ActorController {
    
    @Autowired
    private ActorRepository actorRepository;
    @GetMapping("/actors")
    public String actors(Model model) {
        List<Actor> actors = actorRepository.findAll();
        model.addAttribute("actors", actors);
        return "actors";
    }
    @GetMapping("/actor-form")
    public String actorForm(Model model) {
        Actor actor = new Actor();
        model.addAttribute("actor", actor);
        return "actor-form";
    }
     @PostMapping("/actors/create")
    public String createActor(@RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam String bio,
                              @RequestParam String contactInfo,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate) {

        Actor actor = new Actor();
        actor.setFirstName(firstName);
        actor.setLastName(lastName);
        actor.setBio(bio);
        actor.setContactInfo(contactInfo);
        actor.setBirthDate(birthDate);

        actorRepository.save(actor);

        return "redirect:/actors"; 
    }
    @PostMapping("/actors/delete/{id}")
public String deleteActor(@PathVariable Integer id) {
    actorRepository.deleteById(id);
    return "redirect:/actors"; 
}
@GetMapping("/actors/edit/{id}")
public String showEditForm(@PathVariable Integer id, Model model) {
    Actor actor = actorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Неверный id актера: " + id));
    model.addAttribute("actor", actor);
    return "actor-edit-form";
}

@PostMapping("/actors/edit/{id}")
public String updateActor(@PathVariable Integer id, @ModelAttribute Actor updatedActor) {
    Actor existingActor = actorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Актер не найден: " + id));

    existingActor.setFirstName(updatedActor.getFirstName());
    existingActor.setLastName(updatedActor.getLastName());
    existingActor.setBio(updatedActor.getBio());
    existingActor.setContactInfo(updatedActor.getContactInfo());
    existingActor.setBirthDate(updatedActor.getBirthDate());

    actorRepository.save(existingActor);
    return "redirect:/actors";
}

    
    
}
