package com.example.apa_project.entity;
import jakarta.persistence.*;

@Entity
public class CompanyRepresentative extends User{
	//
	private String positionTitle;
	
	
	public CompanyRepresentative() {
		
	}

	public CompanyRepresentative(String name, String email , String password , String positionTitle) {
		super(name,email,password);
		this.positionTitle = positionTitle;
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}

	
	
}

