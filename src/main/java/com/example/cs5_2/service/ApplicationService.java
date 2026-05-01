package com.example.cs5_2.service;

import com.example.cs5_2.model.*;
import com.example.cs5_2.repository.ApplicationRepository;
import com.example.cs5_2.repository.BuildCVRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final InternshipService internshipService;
    private final BuildCVRepository buildCVRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              InternshipService internshipService,
                              BuildCVRepository buildCVRepository) {
        this.applicationRepository = applicationRepository;
        this.internshipService = internshipService;
        this.buildCVRepository = buildCVRepository;
    }

    // APPLY
    public void addApplication(long studentId,
                               String studentName,
                               Long internshipId) {

        Internship internship = internshipService.findById(internshipId);

        if (internship == null) {
            throw new IllegalArgumentException("Internship not found");
        }

        // lightweight student object (your current design)
        Student student = new Student();
        student.setId(studentId);
        student.setName(studentName);

        // AUTO GET CV FROM DB
        BuildCV cv = buildCVRepository.findByStudent(student);

        if (cv == null) {
            throw new IllegalArgumentException("Student has no CV");
        }

        Application app = new Application();
        app.setApplicationDate(LocalDateTime.now());
        app.setStatus(ApplicationStatus.PENDING);

        app.setStudent(student);
        app.setInternship(internship);
        app.setBuildCV(cv);

        app.setMatchScore(calculateMatchScore(student, internship));

        applicationRepository.save(app);
    }

    // GET ALL
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    // UPDATE STATUS
    public void updateStatus(int id, ApplicationStatus status) {

        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(status);
        applicationRepository.save(app);
    }

    // MATCH SCORE
    private int calculateMatchScore(Student student, Internship internship) {
        int score = 0;

        if (student.getMajor() != null &&
                internship.getRequirements() != null &&
                internship.getRequirements().toLowerCase()
                        .contains(student.getMajor().toLowerCase())) {
            score += 50;
        }

        return score;
    }
}