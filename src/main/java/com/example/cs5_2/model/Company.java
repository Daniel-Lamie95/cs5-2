package com.example.cs5_2.model;

import com.example.cs5_2.model.Internship;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Company extends User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String field;
    private String location;
    private String website;

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
    


    public Long getId() {
		return id;
	}

    public void setId(Long id) {
        this.id = id;
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