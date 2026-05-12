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
	    
	 if (studentName.length() < 3) {

         throw new RuntimeException(
                 "Student name must be at least 3 characters"
         );
	 }
	 }
	 //Status validation
	 public static void validateStatus(Object status) {

	        if (status == null) {

	            throw new RuntimeException(
	                    "Application status is required"
	            );
	        }
	    }
	 //Match score validation
	 public static void validateMatchScore(int score) {

	        if (score < 0 || score > 100) {

	            throw new RuntimeException(
	                    "Match score must be between 0 and 100"
	            );
	        }
	    }
	 //CV validation
	 public static void validateCV(Object cv) {

	        if (cv == null) {

	            throw new RuntimeException(
	                    "Student must upload CV before applying"
	            );
	        }
	    }
}
     


