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
    
    
    public static void maxLength(String value, int length, String message) {
        if (value != null && value.trim().length() > length) {
            throw new ValidationException(message);
        }
    }

    public static void containsLetter(String value, String message) {
        if (value == null || !value.matches(".*[A-Za-z].*")) {
            throw new ValidationException(message);
        }
    }

    public static void matches(String value, String regex, String message) {
        if (value == null || !value.matches(regex)) {
            throw new ValidationException(message);
        }
    }

   
    public static void isEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ValidationException("Invalid email format");
        }
    }
    public static void passwordStrong(String password) {
    	 if (password == null || password.trim().length() < 8) {
    	        throw new ValidationException("Password must be at least 8 characters");
    	    }

    	    if (!password.matches(".*[A-Z].*")) {
    	        throw new ValidationException("Password must contain uppercase letter");
    	    }

    	    if (!password.matches(".*[a-z].*")) {
    	        throw new ValidationException("Password must contain lowercase letter");
    	    }

    	    if (!password.matches(".*[0-9].*")) {
    	        throw new ValidationException("Password must contain number");
    	    }
    }
}

