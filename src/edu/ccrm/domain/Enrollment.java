package edu.ccrm.domain;

import java.time.LocalDateTime;

public class Enrollment {
    private Student student;
    private Course course;
    private LocalDateTime enrollmentDate;
    private Grade grade;
    
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDateTime.now();
        this.grade = Grade.NOT_GRADED;
    }
    
    public void assignGrade(Grade grade) {
        this.grade = grade;
    }
    
    // Getters
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public Grade getGrade() { return grade; }
    public double getGradePoints() { return grade.getPoints(); }
    
    @Override
    public String toString() {
        return String.format("Enrollment{student=%s, course=%s, grade=%s}", 
                           student.getRegNo(), course.getCode(), grade);
    }
}