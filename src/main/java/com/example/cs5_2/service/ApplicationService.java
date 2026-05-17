package com.example.cs5_2.service;
import com.example.cs5_2.allvalidations.ApplicationValidation;
import com.example.cs5_2.model.*;
import com.example.cs5_2.repository.ApplicationRepository;
import com.example.cs5_2.repository.BuildCVRepository;
import com.example.cs5_2.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final InternshipService internshipService;
    private final BuildCVRepository buildCVRepository;
    private final StudentRepository studentRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              InternshipService internshipService,
                              BuildCVRepository buildCVRepository,
                              StudentRepository studentRepository) {
        this.applicationRepository = applicationRepository;
        this.internshipService = internshipService;
        this.buildCVRepository = buildCVRepository;
        this.studentRepository = studentRepository;
    }

    // APPLY
    public void addApplication(long studentId,
                               String studentName,
                               Long internshipId) {
    	ApplicationValidation.validate(
    	        (int) studentId,
    	        studentName,
    	        internshipId
    	);

        Internship internship = internshipService.findById(internshipId);

        if (internship == null) {
            throw new IllegalArgumentException("Internship not found");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if (student.getName() == null && studentName != null) {
            student.setName(studentName);
        }

        // AUTO GET CV FROM DB
        BuildCV cv = buildCVRepository.findByStudent(student);
        ApplicationValidation.validateCV(cv);


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

        ApplicationValidation.validateMatchScore(
                app.getMatchScore()
        );

        applicationRepository.save(app);

    }

    // GET ALL
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    // UPDATE STATUS


    // MATCH SCORE
    private int calculateMatchScore(Student student, Internship internship) {
        int score = 0;

        if (student.getMajor() != null &&
                internship.getDescription() != null &&
                internship.getDescription().toLowerCase()
                        .contains(student.getMajor().toLowerCase())) {
            score += 50;
        }

        return score;
    }

    public List<Application> getApplicationsByCompany(String companyName) {
        return applicationRepository.findByInternshipCompanyName(companyName);
    }

    // Check if student already applied to this internship
    public boolean hasStudentApplied(long studentId, long internshipId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Internship internship = internshipService.findById(internshipId);
        if (internship == null) {
            return false;
        }
        return applicationRepository.existsByStudentAndInternship(student, internship);
    }

    // Get application for a student-internship pair (if exists)
    public Application getStudentApplicationForInternship(long studentId, long internshipId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        List<Application> studentApps = applicationRepository.findByStudent(student);

        for (Application app : studentApps) {
            if (app.getInternship() != null && app.getInternship().getId().equals(internshipId)) {
                return app;
            }
        }
        return null;
    }

    // Revoke/withdraw an application
    public void revokeApplication(long studentId, long internshipId) {
        Application app = getStudentApplicationForInternship(studentId, internshipId);
        if (app == null) {
            throw new IllegalArgumentException("Application not found");
        }

        Student student = app.getStudent();
        Internship internship = app.getInternship();

        // Remove from student_internship table if status was ACCEPTED
        if (app.getStatus() == ApplicationStatus.ACCEPTED && student != null && internship != null) {
            student.removeAppliedInternship(internship);
            studentRepository.save(student);
        }

        // Delete the application
        applicationRepository.delete(app);
    }

    // ...existing code...

    public void updateStatus(Integer applicationId, String status) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + applicationId));

        ApplicationStatus newStatus;
        try {
            newStatus = ApplicationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }

        // ================ MAIN BUSINESS RULE ================
        if (application.isFinalStatus()) {
            throw new IllegalStateException(
                    "This application is already " + application.getStatus() +
                            ". You cannot change its status anymore."
            );
        }

        // Prevent going back to PENDING from any other status
        if (newStatus == ApplicationStatus.PENDING &&
                application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Cannot change status back to PENDING");
        }

        // Update status
        application.setStatus(newStatus);
        applicationRepository.save(application);

        // Optional: Add student to internship list only when ACCEPTED
        if (newStatus == ApplicationStatus.ACCEPTED) {
            Student student = application.getStudent();
            Internship internship = application.getInternship();

            if (student != null && internship != null) {
                student.addAppliedInternship(internship);
                studentRepository.save(student);
            }
        }
    }




    public List<Application> getApplicationsByStudent(Student student) {
        if (student == null) {
            return List.of();
        }
        return applicationRepository.findByStudent(student);
    }
}