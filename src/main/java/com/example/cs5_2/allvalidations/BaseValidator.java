package com.example.cs5_2.allvalidations;

public class BaseValidator {
	
	public static void notNull(Object value, String message) {
        if (value == null) {
            throw new ValidationException(message);
        }
    }

    public static void notEmpty(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(message);
        }
    }

    public static void minLength(String value, int length, String message) {
        if (value == null || value.trim().length() < length) {
            throw new ValidationException(message);
        }
    }

    public static void isEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format");
        }
    }

    public static void passwordStrong(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }
    }
}

