package edu.ccrm.util;

import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern REG_NO_PATTERN = 
        Pattern.compile("^[A-Za-z]{2,3}\\d{3,5}$");
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidRegNo(String regNo) {
        return regNo != null && REG_NO_PATTERN.matcher(regNo).matches();
    }
    
    public static boolean isValidCredits(int credits) {
        return credits > 0 && credits <= 6;
    }
    
    public static void validateStudentData(String id, String regNo, String email) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }
        if (!isValidRegNo(regNo)) {
            throw new IllegalArgumentException("Invalid registration number format: " + regNo);
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
}