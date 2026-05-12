package com.example.cs5_2.allvalidations;
import com.example.cs5_2.model.Internship;
import java.time.LocalDate;

public class InternshipValidation {
	
	public static void validateAdd(Internship i) {
        BaseValidator.notNull(i, "Internship required");
        validateTitle(i.getTitle());
        validateDuration(i.getDuration());
        validateMaxApplicants(i.getMaxApplicants());
        validateDescription(i.getDescription());
        validateDates(i.getStartDate(), i.getEndDate());
    }

    public static void validateTitle(String title) {
        BaseValidator.notEmpty(title, "Title is required");
        BaseValidator.minLength(title, 3, "Title must be at least 3 characters");
        BaseValidator.maxLength(title, 100, "Title must not exceed 100 characters");
    }

    public static void validateDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw new ValidationException("Duration must be a positive number");
        }
        if (duration > 365) {
            throw new ValidationException("Duration cannot exceed 365 days");
        }
    }

    public static void validateMaxApplicants(Integer maxApplicants) {
        if (maxApplicants == null) {
            throw new ValidationException("Maximum applicants count is required");
        }
        if (maxApplicants <= 0) {
            throw new ValidationException("Maximum applicants must be a positive number");
        }
        if (maxApplicants > 10000) {
            throw new ValidationException("Maximum applicants cannot exceed 10,000");
        }
    }

    public static void validateDescription(String description) {
        if (description != null && !description.trim().isEmpty()) {
            String trimmed = description.trim();
            if (trimmed.length() < 10) {
                throw new ValidationException("Description must be at least 10 characters");
            }
            if (trimmed.length() > 5000) {
                throw new ValidationException("Description must not exceed 5000 characters");
            }
        }
    }

    public static void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new ValidationException("Start date cannot be after end date");
            }
            if (startDate.isBefore(LocalDate.now())) {
                throw new ValidationException("Start date cannot be in the past");
            }
        }
    }

}
