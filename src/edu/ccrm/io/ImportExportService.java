package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class ImportExportService {
    private StudentService studentService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;
    private CSVParser csvParser;
    
    public ImportExportService(StudentService studentService, CourseService courseService, 
                             EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.csvParser = new CSVParser();
    }
    
    public void importStudentsFromCSV(String filename) throws IOException {
        Path path = Paths.get(filename);
        
        // Use the CSVParser for more robust parsing
        List<Student> students = CSVParser.parseStudentsFromCSV(path);
        
        for (Student student : students) {
            try {
                studentService.addStudent(student);
                System.out.println("Imported student: " + student.getFullName());
            } catch (Exception e) {
                System.err.println("Error importing student: " + e.getMessage());
            }
        }
    }
    
    public void importCoursesFromCSV(String filename) throws IOException {
        Path path = Paths.get(filename);
        
        List<Course> courses = CSVParser.parseCoursesFromCSV(path);
        
        for (Course course : courses) {
            try {
                courseService.addCourse(course);
                System.out.println("Imported course: " + course.getTitle());
            } catch (Exception e) {
                System.err.println("Error importing course: " + e.getMessage());
            }
        }
    }
    
    public void exportStudentsToCSV(String filename) throws IOException {
        Path path = Paths.get(filename);
        List<Student> students = studentService.findAll();
        
        // Use CSVParser for CSV generation
        List<String> csvLines = CSVParser.convertStudentsToCSV(students);
        
        Files.write(path, csvLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Exported " + students.size() + " students to " + filename);
    }
    
    public void exportCoursesToCSV(String filename) throws IOException {
        Path path = Paths.get(filename);
        List<Course> courses = courseService.findAll();
        
        List<String> csvLines = CSVParser.convertCoursesToCSV(courses);
        
        Files.write(path, csvLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Exported " + courses.size() + " courses to " + filename);
    }
    
    public void exportEnrollmentsToCSV(String filename) throws IOException {
        Path path = Paths.get(filename);
        
        // This would require additional methods to get all enrollments
        // For now, create a simple header
        List<String> lines = List.of("StudentID,CourseCode,EnrollmentDate,Grade");
        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    /**
     * Advanced import with validation
     */
    public void importStudentsWithValidation(String filename) throws IOException {
        Path path = Paths.get(filename);
        
        // Validate CSV structure first
        if (!CSVParser.validateCSVStructure(path, 5)) {
            throw new IOException("Invalid CSV structure for students file");
        }
        
        // Print statistics
        CSVParser.printCSVStatistics(path);
        
        // Then import
        importStudentsFromCSV(filename);
    }
    
    /**
     * Demonstrate advanced CSV parsing features
     */
    public void demonstrateCSVFeatures() {
        CSVParser.demonstrateCSVParsing();
        
        // Example of using the advanced parser
        CSVParser.AdvancedCSVParser advancedParser = new CSVParser.AdvancedCSVParser("|", true);
        System.out.println("Advanced CSV parser ready for pipe-delimited files");
    }
}