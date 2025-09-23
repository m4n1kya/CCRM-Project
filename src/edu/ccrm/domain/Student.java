package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student extends Person {
    private String regNo;
    private List<Enrollment> enrollments;
    
    // Static nested class for Student Builder
    public static class Builder {
        private String id;
        private String regNo;
        private String fullName;
        private String email;
        
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        public Builder regNo(String regNo) {
            this.regNo = regNo;
            return this;
        }
        
        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Student build() {
            return new Student(this);
        }
    }
    
    private Student(Builder builder) {
        super(builder.id, builder.fullName, builder.email);
        this.regNo = Objects.requireNonNull(builder.regNo, "Registration number cannot be null");
        this.enrollments = new ArrayList<>();
    }
    
    public void enrollInCourse(Course course) {
        Enrollment enrollment = new Enrollment(this, course);
        enrollments.add(enrollment);
    }
    
    public void unenrollFromCourse(Course course) {
        enrollments.removeIf(e -> e.getCourse().equals(course) && e.getStudent().equals(this));
    }
    
    public List<Enrollment> getEnrollments() {
        return new ArrayList<>(enrollments); // Defensive copy
    }
    
    public String getRegNo() { return regNo; }
    
    @Override
    public void displayProfile() {
        System.out.println("Student Profile:");
        System.out.println("ID: " + getId());
        System.out.println("Reg No: " + regNo);
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Status: " + (isActive() ? "Active" : "Inactive"));
    }
    
    @Override
    public String getType() {
        return "Student";
    }
    
    @Override
    public String toString() {
        return String.format("Student{id='%s', regNo='%s', name='%s'}", 
                           getId(), regNo, getFullName());
    }
}