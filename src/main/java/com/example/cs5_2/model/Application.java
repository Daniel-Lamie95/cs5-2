package com.example.cs5_2.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Application implements Serializable {

    private int applicationId;
    private LocalDateTime applicationDate;
    private String status;

    // Relationships
    private Student student;
    private Internship internship;

    // Default constructor
    public Application() {
    }

    // Parameterized constructor
    public Application(int applicationId, LocalDateTime applicationDate, String status, Student student, Internship internship) {
        this.applicationId = applicationId;
        this.applicationDate = applicationDate;
        this.status = status;
        this.student = student;
        this.internship = internship;
    }

    // Getters and Setters
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Internship getInternship() {
        return internship;
    }

    public void setInternship(Internship internship) {
        this.internship = internship;
    }

    @Override
    public String toString() {
        return "Application{" +
                "applicationId=" + applicationId +
                ", applicationDate=" + applicationDate +
                ", status='" + status + '\'' +
                ", student=" + (student != null ? student.getName() : "null") +
                ", internship=" + (internship != null ? internship.getTitle() : "null") +
                '}';
    }
}