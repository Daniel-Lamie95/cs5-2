package com.example.cs5_2.allvalidations;
public class ApplicationValidation {
	 public static void validate(int id, String studentName, Long internshipId) {

	        if (id <= 0) {
	            throw new RuntimeException("Invalid application ID");
	        }

	        BaseValidator.notEmpty(studentName, "Student name required");

	        if (internshipId == null || internshipId <= 0) {
	            throw new RuntimeException("Invalid internship ID");
	        }
	    }

}
