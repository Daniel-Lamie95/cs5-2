package com.example.cs5_2.allvalidations;
import com.example.cs5_2.model.Internship;

public class InternshipValidation {
	
	public static void validateAdd(Internship i) {

        BaseValidator.notNull(i, "Internship required");

        BaseValidator.notEmpty(i.getTitle(), "Title required");
        BaseValidator.notEmpty(i.getCompanyName(), "Company name required");

        if (i.getDuration() <= 0) {
            throw new RuntimeException("Duration must be > 0");
        }
    }

}
