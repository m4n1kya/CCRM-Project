package edu.ccrm.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Transcript {
    private Student student;
    private List<Enrollment> enrollments;
    
    public static class Builder {
        private Student student;
        private List<Enrollment> enrollments;
        
        public Builder student(Student student) {
            this.student = student;
            return this;
        }
        
        public Builder enrollments(List<Enrollment> enrollments) {
            this.enrollments = enrollments;
            return this;
        }
        
        public Transcript build() {
            return new Transcript(this);
        }
    }
    
    private Transcript(Builder builder) {
        this.student = builder.student;
        this.enrollments = builder.enrollments != null ? 
            builder.enrollments.stream().collect(Collectors.toList()) : List.of();
    }
    
    public double calculateGPA() {
        if (enrollments.isEmpty()) return 0.0;
        
        double totalPoints = enrollments.stream()
            .filter(e -> e.getGrade() != Grade.NOT_GRADED)
            .mapToDouble(e -> e.getGrade().getPoints() * e.getCourse().getCredits())
            .sum();
            
        int totalCredits = enrollments.stream()
            .filter(e -> e.getGrade() != Grade.NOT_GRADED)
            .mapToInt(e -> e.getCourse().getCredits())
            .sum();
            
        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }
    
    public void display() {
        System.out.println("=== TRANSCRIPT ===");
        student.displayProfile();
        System.out.println("\nCourses:");
        System.out.println("Code\tTitle\t\tCredits\tGrade\tPoints");
        System.out.println("----------------------------------------");
        
        enrollments.forEach(e -> {
            System.out.printf("%s\t%s\t%d\t%s\t%.1f\n",
                e.getCourse().getCode(),
                e.getCourse().getTitle().substring(0, Math.min(10, e.getCourse().getTitle().length())),
                e.getCourse().getCredits(),
                e.getGrade(),
                e.getGradePoints());
        });
        
        System.out.printf("Overall GPA: %.2f\n", calculateGPA());
    }
    
    @Override
    public String toString() {
        return String.format("Transcript{student=%s, courses=%d, GPA=%.2f}", 
                           student.getRegNo(), enrollments.size(), calculateGPA());
    }
}