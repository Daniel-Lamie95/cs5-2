package com.example.cs5_2.allvalidations;
import com.example.cs5_2.model.Student;

public class StudentValidation {
	
	 public static void validateRegister(Student s) {

	        BaseValidator.notNull(s, "Student is required");

	        BaseValidator.notEmpty(s.getName(), "Name is required");
	        BaseValidator.minLength(s.getName(), 3, "Name must be at least 3 chars");

	        BaseValidator.isEmail(s.getEmail());
	        BaseValidator.passwordStrong(s.getPassword());

	        BaseValidator.notEmpty(s.getMajor(), "Major is required");
	        BaseValidator.notEmpty(s.getUniversity(), "University is required");
	    }

	    public static void validateUpdate(Student s) {

	        BaseValidator.notNull(s, "Student is required");

	        BaseValidator.notEmpty(s.getName(), "Name is required");
	        BaseValidator.notEmpty(s.getMajor(), "Major is required");
	        BaseValidator.notEmpty(s.getUniversity(), "University is required");
	    }
	}
	


