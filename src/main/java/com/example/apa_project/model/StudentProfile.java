package com.example.apa_project.model;

public class StudentProfile {
    private String name;
    private String email;
    private String major;

    public StudentProfile() {
    }

    public StudentProfile(String name, String email, String major) {
        this.name = name;
        this.email = email;
        this.major = major;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}

