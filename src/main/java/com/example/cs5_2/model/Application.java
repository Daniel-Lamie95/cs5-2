package com.example.cs5_2.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Application implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int applicationId;

    private LocalDateTime applicationDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private int matchScore;

    private LocalDateTime interviewDate;
    private String interviewMode;
    private String interviewLocation;

    // RELATIONS

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "internship_id")
    private Internship internship;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private BuildCV buildCV;

    public Application(int applicationId, LocalDateTime applicationDate, ApplicationStatus status, int matchScore, LocalDateTime interviewDate, String interviewMode, String interviewLocation, Student student, Internship internship, BuildCV buildCV) {
        this.applicationId = applicationId;
        this.applicationDate = applicationDate;
        this.status = status;
        this.matchScore = matchScore;
        this.interviewDate = interviewDate;
        this.interviewMode = interviewMode;
        this.interviewLocation = interviewLocation;
        this.student = student;
        this.internship = internship;
        this.buildCV = buildCV;
    }

    public Application() {}

    // GETTERS & SETTERS

    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public LocalDateTime getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDateTime applicationDate) { this.applicationDate = applicationDate; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public int getMatchScore() { return matchScore; }
    public void setMatchScore(int matchScore) { this.matchScore = matchScore; }

    public LocalDateTime getInterviewDate() { return interviewDate; }
    public void setInterviewDate(LocalDateTime interviewDate) { this.interviewDate = interviewDate; }

    public String getInterviewMode() { return interviewMode; }
    public void setInterviewMode(String interviewMode) { this.interviewMode = interviewMode; }

    public String getInterviewLocation() { return interviewLocation; }
    public void setInterviewLocation(String interviewLocation) { this.interviewLocation = interviewLocation; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Internship getInternship() { return internship; }
    public void setInternship(Internship internship) { this.internship = internship; }

    public BuildCV getBuildCV() { return buildCV; }
    public void setBuildCV(BuildCV buildCV) { this.buildCV = buildCV; }
}