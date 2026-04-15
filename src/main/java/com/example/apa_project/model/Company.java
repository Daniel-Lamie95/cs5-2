package com.example.apa_project.model;
import jakarta.persistence.*;
import java.util.List;


@Entity
public class Company extends User {

private String field;
private String location;
private String description;

@OneToMany(mappedBy = "company")
private List<Internship> internships;

  
public Company() {
	
}

public Company(int id ,String name, String email, String password,String companyName, String field,
	                   String location, String description) {
	
	        super(id,name, email, password);
	        this.field = field;
	        this.location = location;
	        this.description = description;
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

