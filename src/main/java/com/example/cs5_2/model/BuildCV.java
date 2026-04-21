package com.example.cs5_2.model;
import java.util.ArrayList;
import java.util.List;

public class BuildCV {
    private int id;
    private int studentId;
    private String name;
    private String jobTitle;
    private String email;
    private String phone;
    private String location;
    private String summary;
    private String education;
    private String skills;
    private String certifications;
    private List<ExperienceEntry> experiences = new ArrayList<>();

    public static class ExperienceEntry {
        private String title; 
        private String organization; 
        private String description; 
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getOrganization() { return organization; }
        public void setOrganization(String organization) { this.organization = organization; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getCertifications() { return certifications; }
    public void setCertifications(String certifications) { this.certifications = certifications; }
    public List<ExperienceEntry> getExperiences() { return experiences; }
    public void setExperiences(List<ExperienceEntry> experiences) { this.experiences = experiences; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
}