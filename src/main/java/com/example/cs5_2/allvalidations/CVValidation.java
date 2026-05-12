package com.example.cs5_2.allvalidations;
import com.example.cs5_2.model.BuildCV;
import java.util.regex.Pattern;


public class CVValidation { 
		     private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s\\-\\'\\.]+$");

		     public static void validate(BuildCV cv) {
		   
		         BaseValidator.notNull(cv, "CV data is missing");

		         //Name Validation 
		         BaseValidator.notEmpty(cv.getName(), "Full Name is required");
		         if (!NAME_PATTERN.matcher(cv.getName()).matches()) {
		             throw new IllegalArgumentException("Names cannot contain numbers or special characters.");
		         }
		         if (cv.getName().trim().length() < 2) {
		             throw new IllegalArgumentException("Name is too short.");
		         }

		         //Email Validation
		         BaseValidator.notEmpty(cv.getEmail(), "Email is required");
		         if (!cv.getEmail().contains("@") || !cv.getEmail().contains(".")) {
		             throw new IllegalArgumentException("Please provide a valid email address.");
		         }

		         // Experience Validation 
		         if (cv.getExperiences() != null) {
		             for (BuildCV.ExperienceEntry exp : cv.getExperiences()) {
		                 if (!exp.getTitle().isEmpty() || !exp.getOrganization().isEmpty()) {
		                    
		                     if (exp.getOrganization().trim().length() < 2) {
		                         throw new IllegalArgumentException("Organization name '" + exp.getOrganization() + "' is too short. Please provide the full company name.");
		                     }
		                
		                     if (exp.getTitle().trim().length() < 2) {
		                         throw new IllegalArgumentException("Job title is too short.");
		                     }
		                 }
		             }
		         }

		         //Education Validation
		         if (cv.getEducationList() != null) {
		             for (BuildCV.EducationEntry edu : cv.getEducationList()) {
		                 if (edu.getInstitution() != null && !edu.getInstitution().isEmpty()) {
		                     if (edu.getInstitution().trim().length() < 3) {
		                         throw new IllegalArgumentException("Institution name is too short. Please use the full name of your school/university.");
		                     }
		                 }
		             }
		         }
		     }
		 }


