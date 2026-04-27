package com.example.cs5_2.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "build_cv")
public class BuildCV {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

 
    private String name;
    private String jobTitle;
    private String email;
    private String location;
    
    @Column(columnDefinition = "TEXT")
    private String skills;
    
    @Column(columnDefinition = "TEXT")
    private String certifications;

   
    @ElementCollection
    @CollectionTable(name = "cv_education", joinColumns = @JoinColumn(name = "cv_id"))
    private List<EducationEntry> educationList = new ArrayList<>();

   
    @ElementCollection
    @CollectionTable(name = "cv_experience", joinColumns = @JoinColumn(name = "cv_id"))
    private List<ExperienceEntry> experiences = new ArrayList<>();

    
    public BuildCV() {
        if (educationList.isEmpty()) {
            educationList.add(new EducationEntry("High School", ""));
            educationList.add(new EducationEntry("Bachelor's Degree", ""));
            educationList.add(new EducationEntry("Master's Degree", ""));
            educationList.add(new EducationEntry("Doctorate (PhD)", ""));
        }
    }

  
    @Embeddable
    public static class EducationEntry {
        private String degree; // e.g., "High School"
        private String detail; // e.g., "Cairo American College (2022)"

        public EducationEntry() {}
        public EducationEntry(String degree, String detail) {
            this.degree = degree;
            this.detail = detail;
        }
        // Getters and Setters
        public String getDegree() { return degree; }
        public void setDegree(String degree) { this.degree = degree; }
        public String getDetail() { return detail; }
        public void setDetail(String detail) { this.detail = detail; }
    }

    @Embeddable
    public static class ExperienceEntry {
        private String title;
        private String organization;
        @Column(columnDefinition = "TEXT")
        private String description;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getOrganization() { return organization; }
        public void setOrganization(String organization) { this.organization = organization; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

   
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getCertifications() { return certifications; }
    public void setCertifications(String certifications) { this.certifications = certifications; }

    public List<EducationEntry> getEducationList() { return educationList; }
    public void setEducationList(List<EducationEntry> educationList) { this.educationList = educationList; }

    public List<ExperienceEntry> getExperiences() { return experiences; }
    public void setExperiences(List<ExperienceEntry> experiences) { this.experiences = experiences; }
}