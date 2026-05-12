package com.example.cs5_2.model;

import com.example.cs5_2.model.Internship;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Company extends User {
    private String field;
    private String location;
    private String website;
    private String phone;
    private String logo;
    private double rating = 0.0;
    private int ratingCount = 0;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "company")
    private List<Internship> internships= new ArrayList<>();

    public Company() {
    }

    public Company(String name, String email, String password, String field, String location, String website, String description) {

        super(name, email, password);
        this.field = field;
        this.location = location;
        this.website = website;
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


	
	public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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

    
    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
    public void addRating(double newRating) {
        this.rating =
            ((this.rating * this.ratingCount) + newRating)
            / (this.ratingCount + 1);

        this.ratingCount++;
    }
}