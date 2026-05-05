package com.example.cs5_2.allvalidations;
import com.example.cs5_2.model.Company;

public class CompanyValidation {
	 public static void validateRegister(Company c) {

	        BaseValidator.notNull(c, "Company is required");

	        BaseValidator.notEmpty(c.getName(), "Company name is required");
	        BaseValidator.minLength(c.getName(), 3, "Name too short");

	        BaseValidator.isEmail(c.getEmail());
	        BaseValidator.passwordStrong(c.getPassword());

	        BaseValidator.notEmpty(c.getField(), "Field required");
	        BaseValidator.notEmpty(c.getLocation(), "Location required");

	        BaseValidator.minLength(c.getDescription(), 20, "Description too short");
	    }

	    public static void validateUpdate(Company c) {

	        BaseValidator.notNull(c, "Company is required");

	        BaseValidator.notEmpty(c.getName(), "Company name is required");
	        BaseValidator.notEmpty(c.getField(), "Field required");
	        BaseValidator.notEmpty(c.getLocation(), "Location required");
	        BaseValidator.minLength(c.getDescription(), 20, "Description too short");
	    }

}
