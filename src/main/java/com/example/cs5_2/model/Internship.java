package com.example.cs5_2.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Internship implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String companyName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int duration;
    private String requirements;
    private int applicantsCount;
    private int maxApplicants;

    public Internship() {}

    public Internship(String title, String companyName, LocalDate startDate, LocalDate endDate, int duration, String requirements) {
        this.title = title;
        this.companyName = companyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.requirements = requirements;
    }

    @ManyToMany(mappedBy = "appliedInternships")
    private List<Student> students = new ArrayList<>();
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public int getMaxApplicants() {
        return maxApplicants;
    }

    public void setMaxApplicants(int maxApplicants) {
        this.maxApplicants = maxApplicants;
    }

    public int getApplicantsCount() {
        return applicantsCount;
    }

    public void setApplicantsCount(int applicantsCount) {
        this.applicantsCount = applicantsCount;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Internship{" +
                "title='" + title + '\'' +
                ", companyName='" + companyName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", duration=" + duration +
                ", requirements='" + requirements + '\'' +
                ", applicantsCount=" + applicantsCount +
                ", maxApplicants=" + maxApplicants +
                '}';
    }
}
