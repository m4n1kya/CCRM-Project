package edu.ccrm.domain;

import java.util.Objects;

// Immutable value class
public final class CourseCode {
    private final String code;
    private final String prefix;
    private final int number;
    
    public CourseCode(String code) {
        this.code = Objects.requireNonNull(code, "Course code cannot be null").toUpperCase().trim();
        
        // Parse prefix and number (e.g., "CS101" -> "CS" and 101)
        int i = 0;
        while (i < code.length() && Character.isLetter(code.charAt(i))) {
            i++;
        }
        
        if (i == 0 || i == code.length()) {
            throw new IllegalArgumentException("Invalid course code format: " + code);
        }
        
        this.prefix = code.substring(0, i);
        try {
            this.number = Integer.parseInt(code.substring(i));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid course number in: " + code);
        }
    }
    
    public String getCode() { return code; }
    public String getPrefix() { return prefix; }
    public int getNumber() { return number; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CourseCode that = (CourseCode) obj;
        return code.equals(that.code);
    }
    
    @Override
    public int hashCode() {
        return code.hashCode();
    }
    
    @Override
    public String toString() {
        return code;
    }
}