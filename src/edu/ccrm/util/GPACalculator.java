package edu.ccrm.util;

import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Grade;

import java.util.List;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

public class GPACalculator {
    
    public static double calculateGPA(List<Enrollment> enrollments) {
        if (enrollments == null || enrollments.isEmpty()) {
            return 0.0;
        }
        
        List<Enrollment> gradedEnrollments = enrollments.stream()
                .filter(e -> e.getGrade() != Grade.NOT_GRADED)
                .collect(Collectors.toList());
        
        if (gradedEnrollments.isEmpty()) {
            return 0.0;
        }
        
        double totalPoints = gradedEnrollments.stream()
                .mapToDouble(e -> e.getGrade().getPoints() * e.getCourse().getCredits())
                .sum();
                
        int totalCredits = gradedEnrollments.stream()
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();
                
        return totalPoints / totalCredits;
    }
    
    public static DoubleSummaryStatistics getGPAStatistics(List<Enrollment> enrollments) {
        return enrollments.stream()
                .filter(e -> e.getGrade() != Grade.NOT_GRADED)
                .mapToDouble(e -> e.getGrade().getPoints())
                .summaryStatistics();
    }
    
    // Lambda expressions for different GPA calculation strategies
    public static final java.util.function.Function<List<Enrollment>, Double> STANDARD_GPA = 
        GPACalculator::calculateGPA;
    
    public static final java.util.function.Function<List<Enrollment>, Double> WEIGHTED_GPA = 
        enrollments -> {
            if (enrollments == null || enrollments.isEmpty()) return 0.0;
            
            return enrollments.stream()
                    .filter(e -> e.getGrade() != Grade.NOT_GRADED)
                    .mapToDouble(e -> e.getGrade().getPoints() * e.getCourse().getCredits())
                    .average()
                    .orElse(0.0);
        };
}