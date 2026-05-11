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
    private String description;
    private String photoPath;
    private int applicantsCount;
    private int maxApplicants;
    private String location;
    private String field;


    public Internship() {}

    public Internship(String title, String companyName, LocalDate startDate, LocalDate endDate, int duration, String description) {
        this.title = title;
        this.companyName = companyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.description = description;
    }

    @ManyToMany(mappedBy = "appliedInternships")
    private List<Student> students = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name= "companyID")
    private Company company;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
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

    public void setId(Long id) {
        this.id = id;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getField() {
        return field;
    }

    public String getLocation() {
        return location;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Internship{" +
                "title='" + title + '\'' +
                ", companyName='" + companyName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", photoPath='" + photoPath + '\'' +
                ", applicantsCount=" + applicantsCount +
                ", maxApplicants=" + maxApplicants +
                '}';
    }
}
