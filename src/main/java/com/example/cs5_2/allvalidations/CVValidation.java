package com.example.cs5_2.allvalidations;
import com.example.cs5_2.model.BuildCV;

public class CVValidation {
	 public static void validate(BuildCV cv) {

	        BaseValidator.notNull(cv, "CV is required");

	        BaseValidator.notEmpty(cv.getName(), "Name required");
	        BaseValidator.notEmpty(cv.getEmail(), "Email required");

	        BaseValidator.notEmpty(cv.getJobTitle(), "Job title required");
	    }

}
