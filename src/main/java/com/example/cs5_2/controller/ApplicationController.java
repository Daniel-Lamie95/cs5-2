package com.example.cs5_2.controller;

import com.example.cs5_2.service.ApplicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Show page
    @GetMapping("/application")
    public String showApplications(Model model) {
        model.addAttribute("applications", applicationService.getAllApplications());
        return "application";
    }

    // Add application
    @PostMapping("/application")
    public String addApplication(@RequestParam int applicationId,
                                 @RequestParam String studentName,
                                 @RequestParam String internshipTitle) {

        applicationService.addApplication(applicationId, studentName, internshipTitle);
        return "redirect:/application";
    }

    // Update status
    @PostMapping("/application/update")
    public String updateStatus(@RequestParam int id,
                               @RequestParam String status) {

        applicationService.updateStatus(id, status);
        return "redirect:/application";
    }
}