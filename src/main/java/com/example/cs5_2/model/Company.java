package com.example.cs5_2.model;

import java.util.ArrayList;
//import jakarta.persistence.*;
import java.util.List;


public class Company extends User {

    private String industry;
    private String location;
    private String website;
    private String description;
    private List <Internship> internships = new ArrayList<>();

    public Company() {
        
    }

    public Company(int id, String name, String email, String password, String role,
                   String industry, String location, String website, String description) {
        super(id, name, email, password, role);
        this.industry = industry;
        this.location = location;
        this.website = website;
        this.description = description;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public List<Internship> getInternships() {
		return internships;
	}

	public void setInternships(List<Internship> internships) {
		this.internships = internships;
	}
    
    
}