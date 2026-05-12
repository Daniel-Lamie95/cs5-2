package com.example.cs5_2.controller;

import com.example.cs5_2.model.ApplicationStatus;
import com.example.cs5_2.service.ApplicationService;
import com.example.cs5_2.service.InternshipService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ApplicationController {

    private final ApplicationService applicationService;
    private final InternshipService internshipService;

    public ApplicationController(ApplicationService applicationService,
                                 InternshipService internshipService) {
        this.applicationService = applicationService;
        this.internshipService = internshipService;
    }

    // SHOW PAGE
    @GetMapping("/application")
    public String showApplications(Model model) {
        model.addAttribute("applications", applicationService.getAllApplications());
        model.addAttribute("internships", internshipService.getAllInternships());
        model.addAttribute("statuses", ApplicationStatus.values());
        return "applications";
    }

    // APPLY WITH PDF CV
    @PostMapping("/application")
    public String addApplication(@RequestParam int studentId,
                                 @RequestParam String studentName,
                                 @RequestParam Long internshipId) {

        applicationService.addApplication(studentId, studentName, internshipId);

        return "redirect:/application";
    }

    // UPDATE STATUS
    @PostMapping("/application/update")
    public String updateStatus(@RequestParam Integer id, @RequestParam String status) {
        applicationService.updateStatus(id, status);
        return "redirect:/application";
    }
}