package com.example.cs5_2.allvalidations;
import com.example.cs5_2.model.Company;

public class CompanyValidation {
	public static void validateRegister(Company c) {

        BaseValidator.notNull(c, "Company is required");

        BaseValidator.notEmpty(c.getName(), "Company name is required");
        BaseValidator.minLength(c.getName(), 2, "Name too short");
        BaseValidator.containsLetter(c.getName(), "Company name must contain letters");

        BaseValidator.isEmail(c.getEmail());

        BaseValidator.passwordStrong(c.getPassword());

        BaseValidator.notEmpty(c.getPhone(), "Phone required");
        BaseValidator.matches(
            c.getPhone(),
            "^(010|011|012|015)[0-9]{8}$",
            "Invalid phone number"
        );


        BaseValidator.notEmpty(c.getField(), "Field required");
        BaseValidator.minLength(c.getField(), 2, "Field too short");

        BaseValidator.notEmpty(c.getLocation(), "Location required");
        BaseValidator.minLength(c.getLocation(), 2, "Location too short");

        if (c.getWebsite() != null && !c.getWebsite().trim().isEmpty()) {
            BaseValidator.matches(
                c.getWebsite(),
                "^(https?://)?([\\w-]+\\.)+[\\w-]{2,}(/.*)?$",
                "Invalid website"
            );
        }

        BaseValidator.notEmpty(c.getDescription(), "Description required");
        BaseValidator.minLength(c.getDescription(), 20, "Description too short");
        BaseValidator.maxLength(c.getDescription(), 1000, "Description too long");
    }

    public static void validateLogin(String email, String password) {

        BaseValidator.notEmpty(email, "Email is required");
        BaseValidator.isEmail(email);

        BaseValidator.notEmpty(password, "Password is required");
    }

    public static void validateGetById(Long id) {

        if (id == null || id <= 0) {
            throw new ValidationException("Invalid company id");
        }
    }

    public static void validateUpdate(Company c) {

        BaseValidator.notNull(c, "Company is required");

        BaseValidator.notEmpty(c.getName(), "Company name is required");
        BaseValidator.minLength(c.getName(), 2, "Name too short");
        BaseValidator.containsLetter(c.getName(), "Company name must contain letters");

        BaseValidator.notEmpty(c.getPhone(), "Phone required");
        BaseValidator.matches(
            c.getPhone(),
            "^(010|011|012|015)[0-9]{8}$",
            "Invalid phone number"
        );

        BaseValidator.notEmpty(c.getField(), "Field required");
        BaseValidator.minLength(c.getField(), 2, "Field too short");

        BaseValidator.notEmpty(c.getLocation(), "Location required");
        BaseValidator.minLength(c.getLocation(), 2, "Location too short");

        if (c.getWebsite() != null && !c.getWebsite().trim().isEmpty()) {
            BaseValidator.matches(
                c.getWebsite(),
                "^(https?://)?([\\w-]+\\.)+[\\w-]{2,}(/.*)?$",
                "Invalid website"
            );
        }

        BaseValidator.notEmpty(c.getDescription(), "Description required");
        BaseValidator.minLength(c.getDescription(), 20, "Description too short");
        BaseValidator.maxLength(c.getDescription(), 1000, "Description too long");
    }
}
