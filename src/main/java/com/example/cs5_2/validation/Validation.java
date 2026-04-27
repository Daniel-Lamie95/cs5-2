package com.example.cs5_2.validation;
import com.example.cs5_2.model.Company;


public class Validation {

	// Company validation 

	    public static void validateRegister(Company company) {

	        if (company == null) {
	            throw new IllegalArgumentException("Company data is required");
	        }

	        if (company.getName() == null || company.getName().trim().length() < 3) {
	            throw new IllegalArgumentException("Company name must be at least 3 characters");
	        }

	        if (company.getEmail() == null || company.getEmail().trim().isEmpty()) {
	            throw new IllegalArgumentException("Email is required");
	        }

	        if (!company.getEmail().contains("@")) {
	            throw new IllegalArgumentException("Invalid email format");
	        }

	        if (company.getPassword() == null || company.getPassword().length() < 8) {
	            throw new IllegalArgumentException("Password must be at least 8 characters");
	        }

	        if (company.getField() == null || company.getField().trim().length() < 3) {
	            throw new IllegalArgumentException("Field must be at least 3 characters");
	        }

	        if (company.getLocation() == null || company.getLocation().trim().length() < 3) {
	            throw new IllegalArgumentException("Location must be at least 3 characters");
	        }

	        if (company.getDescription() == null || company.getDescription().trim().length() < 20) {
	            throw new IllegalArgumentException("Description must be at least 20 characters");
	        }
	    }

	    public static void validateUpdate(Company company) {

	        if (company == null) {
	            throw new IllegalArgumentException("Company data is required");
	        }

	        if (company.getName() == null || company.getName().trim().length() < 3) {
	            throw new IllegalArgumentException("Company name must be at least 3 characters");
	        }

	        if (company.getField() == null || company.getField().trim().length() < 3) {
	            throw new IllegalArgumentException("Field must be at least 3 characters");
	        }

	        if (company.getLocation() == null || company.getLocation().trim().length() < 3) {
	            throw new IllegalArgumentException("Location must be at least 3 characters");
	        }

	        if (company.getDescription() == null || company.getDescription().trim().length() < 20) {
	            throw new IllegalArgumentException("Description must be at least 20 characters");
	        }
	    }
	
}
