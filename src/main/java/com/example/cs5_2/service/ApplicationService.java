package com.example.cs5_2.service;

import com.example.cs5_2.model.Application;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.model.Internship;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {

    private final List<Application> application = new ArrayList<>();
    private final InternshipService internshipService;

    public ApplicationService(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    // Create application
    public void addApplication(int id, String studentName, Long internshipId) {
        try {
            internshipService.applyToInternship(internshipId);

            Application app = new Application();

            app.setApplicationId(id);
            app.setApplicationDate(LocalDateTime.now());
            app.setStatus("Pending");

            Student student = new Student();
            student.setName(studentName);

            Internship internship = internshipService.findById(internshipId);

            app.setStudent(student);
            app.setInternship(internship);

            application.add(app);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot apply to internship: " + e.getMessage());
        }
    }

    // Get all
    public List<Application> getAllApplications() {
        return application;
    }

    // Update status
    public void updateStatus(int id, String status) {
        for (Application app : application) {
            if (app.getApplicationId() == id) {
                app.setStatus(status);
                break;
            }
        }
    }
}