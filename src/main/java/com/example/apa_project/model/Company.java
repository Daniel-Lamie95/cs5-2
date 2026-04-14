package com.example.apa_project.model;
import jakarta.persistence.*;
import java.util.List;

public class Company extends User {
@Entity
private String feild;
private String location;
private String description;

@OneToMany(mappedBy = "company")
private List<Internship> internships;

	  
public Company(int id ,String name, String email, String password,String companyName, String feild,
	                   String location, String description) {
	
	        super(id,name, email, password);
	        this.feild = feild;
	        this.location = location;
	        this.description = description;
	    }

public String getFeild() {
	return feild;
}


public void setFeild(String feild) {
	this.feild = feild;
}

public String getLocation() {
	        return location;
	    }

 public void setLocation(String location) {
	        this.location = location;
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

