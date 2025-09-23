package edu.ccrm.service;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Course;
import edu.ccrm.exception.DuplicateStudentException;
import edu.ccrm.exception.StudentNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StudentService implements Searchable<Student>, Persistable {
    private List<Student> students;
    
    public StudentService() {
        this.students = new ArrayList<>();
    }
    
    public void addStudent(Student student) throws DuplicateStudentException {
        if (findStudentById(student.getId()) != null) {
            throw new DuplicateStudentException("Student with ID " + student.getId() + " already exists");
        }
        students.add(student);
    }
    
    public Student findStudentById(String id) {
        return students.stream()
                .filter(s -> s.getId().equals(id) && s.isActive())
                .findFirst()
                .orElse(null);
    }
    
    public Student findStudentByRegNo(String regNo) {
        return students.stream()
                .filter(s -> s.getRegNo().equals(regNo) && s.isActive())
                .findFirst()
                .orElse(null);
    }
    
    public void updateStudent(String id, String newName, String newEmail) throws StudentNotFoundException {
        Student student = findStudentById(id);
        if (student == null) {
            throw new StudentNotFoundException("Student with ID " + id + " not found");
        }
        student.setFullName(newName);
        student.setEmail(newEmail);
    }
    
    public void deactivateStudent(String id) throws StudentNotFoundException {
        Student student = findStudentById(id);
        if (student == null) {
            throw new StudentNotFoundException("Student with ID " + id + " not found");
        }
        student.setActive(false);
    }
    
    public List<Enrollment> getStudentEnrollments(String studentId) {
        Student student = findStudentById(studentId);
        return student != null ? student.getEnrollments() : List.of();
    }
    
    @Override
    public List<Student> search(Predicate<Student> predicate) {
        return students.stream()
                .filter(predicate)
                .filter(Student::isActive)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Student> findAll() {
        return students.stream()
                .filter(Student::isActive)
                .collect(Collectors.toList());
    }
    
    @Override
    public void saveToFile(String filename) {
        // Implementation in ImportExportService
    }
    
    @Override
    public void loadFromFile(String filename) {
        // Implementation in ImportExportService
    }
    
    // Override default method
    @Override
    public String getSearchDescription() {
        return "Student search service with advanced filtering capabilities";
    }
}