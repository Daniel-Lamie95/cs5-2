package com.example.cs5_2.controller;

import com.example.cs5_2.model.Internship;
import com.example.cs5_2.service.InternshipService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/internships")
public class InternshipController {

    private final InternshipService service;

    public InternshipController(InternshipService service) {
        this.service = service;
    }


    @GetMapping("/new")

    public String showForm(Model model) {
    model.addAttribute("internship", new Internship());
    return "internship-form";
    }


    @PostMapping
    public String createInternship(@ModelAttribute Internship internship, Model model) {
        try {
            String result = service.addInternship(internship);
            model.addAttribute("message", result);
            return "result";
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "internship-form";
        }
    }
    @GetMapping
    public String viewInternships(Model model) {
        model.addAttribute("internships", service.getAllInternships());
        return "internship-list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String title, Model model) {
        try {
            String result = service.deleteInternship(title);
            model.addAttribute("message", result);
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
        }

        model.addAttribute("internships", service.getAllInternships());
        return "internship-list";
    }


    @GetMapping("/edit")
    public String showEditForm(@RequestParam String title, Model model) {
        Internship internship = service.findByTitle(title);

        if (internship == null) {
            model.addAttribute("message", "Internship not found");
            return "result";
        }

        model.addAttribute("internship", internship);
        return "internship-form";
    }


    @PostMapping("/update")
    public String updateInternship(@RequestParam String title,
                                   @ModelAttribute Internship internship,
                                   Model model) {
        try {
            String result = service.updateInternship(title, internship);
            model.addAttribute("message", result);
            return "result";
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "internship-form";
        }
    }
}


