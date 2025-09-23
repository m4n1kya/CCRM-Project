package edu.ccrm.service;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Semester;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.exception.MaxCreditLimitExceededException;
import edu.ccrm.exception.EnrollmentNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnrollmentService {
    private List<Enrollment> enrollments;
    private static final int MAX_CREDITS_PER_SEMESTER = 18;
    
    public EnrollmentService() {
        this.enrollments = new ArrayList<>();
    }
    
    public void enrollStudent(Student student, Course course) 
            throws DuplicateEnrollmentException, MaxCreditLimitExceededException {
        
        // Check for duplicate enrollment
        if (isStudentEnrolled(student, course)) {
            throw new DuplicateEnrollmentException(
                "Student " + student.getRegNo() + " is already enrolled in course " + course.getCode());
        }
        
        // Check credit limit
        if (getCurrentSemesterCredits(student, course.getSemester()) + course.getCredits() > MAX_CREDITS_PER_SEMESTER) {
            throw new MaxCreditLimitExceededException(
                "Credit limit exceeded for student " + student.getRegNo() + 
                ". Current: " + getCurrentSemesterCredits(student, course.getSemester()) + 
                ", Attempting: " + course.getCredits() + 
                ", Max allowed: " + MAX_CREDITS_PER_SEMESTER);
        }
        
        Enrollment enrollment = new Enrollment(student, course);
        enrollments.add(enrollment);
        student.enrollInCourse(course);
    }
    
    public void unenrollStudent(Student student, Course course) throws EnrollmentNotFoundException {
        boolean removed = enrollments.removeIf(e -> 
            e.getStudent().equals(student) && e.getCourse().equals(course));
        
        if (!removed) {
            throw new EnrollmentNotFoundException(
                "Enrollment not found for student " + student.getRegNo() + " in course " + course.getCode());
        }
        
        student.unenrollFromCourse(course);
    }
    
    public void assignGrade(Student student, Course course, Grade grade) throws EnrollmentNotFoundException {
        Enrollment enrollment = findEnrollment(student, course);
        if (enrollment == null) {
            throw new EnrollmentNotFoundException(
                "Enrollment not found for student " + student.getRegNo() + " in course " + course.getCode());
        }
        enrollment.assignGrade(grade);
    }
    
    public void assignGradeByPercentage(Student student, Course course, double percentage) 
            throws EnrollmentNotFoundException {
        Grade grade = Grade.fromPercentage(percentage);
        assignGrade(student, course, grade);
    }
    
    public Enrollment findEnrollment(Student student, Course course) {
        return enrollments.stream()
                .filter(e -> e.getStudent().equals(student) && e.getCourse().equals(course))
                .findFirst()
                .orElse(null);
    }
    
    public List<Enrollment> getEnrollmentsByStudent(Student student) {
        return enrollments.stream()
                .filter(e -> e.getStudent().equals(student))
                .collect(Collectors.toList());
    }
    
    public List<Enrollment> getEnrollmentsByCourse(Course course) {
        return enrollments.stream()
                .filter(e -> e.getCourse().equals(course))
                .collect(Collectors.toList());
    }
    
    private boolean isStudentEnrolled(Student student, Course course) {
        return findEnrollment(student, course) != null;
    }
    
    private int getCurrentSemesterCredits(Student student, Semester semester) {
        return enrollments.stream()
                .filter(e -> e.getStudent().equals(student) && e.getCourse().getSemester() == semester)
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();
    }
    
    public int getEnrollmentCount() {
        return enrollments.size();
    }
}