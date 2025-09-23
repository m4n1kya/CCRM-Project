package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.exception.*;
import edu.ccrm.io.BackupService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.*;
import edu.ccrm.util.RecursionUtil;
import edu.ccrm.util.Validator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Handles menu operations and user interactions.
 */
public class MenuHandler {

    private final Scanner scanner;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final TranscriptService transcriptService;
    private final ImportExportService importExportService;
    private final BackupService backupService;
    private final AppConfig config;

    // This constructor must match the call in CLIInterface.java
    public MenuHandler(Scanner scanner, StudentService studentService, CourseService courseService,
                       EnrollmentService enrollmentService, TranscriptService transcriptService,
                       ImportExportService importExportService, BackupService backupService) {
        this.scanner = scanner;
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.transcriptService = transcriptService;
        this.importExportService = importExportService;
        this.backupService = backupService;
        this.config = AppConfig.getInstance();
    }

    public void handleMainMenu(int choice) {
        switch (choice) {
            case 1 -> manageStudents();
            case 2 -> manageCourses();
            case 3 -> manageEnrollments();
            case 4 -> manageGrades();
            case 5 -> handleImportExport();
            case 6 -> handleBackupOperations();
            case 7 -> generateReports();
            case 8 -> displayJavaInfo();
            case 0 -> System.out.println("Exiting...");
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    // --- Private Menu Methods (these methods need to be implemented) ---
    // These methods fix the "undefined" errors from your console
    private void manageStudents() {
        System.out.println("\n--- Student Management Menu ---");
        // Your student management logic goes here
    }

    private void manageCourses() {
        System.out.println("\n--- Course Management Menu ---");
        // Your course management logic goes here
    }

    private void manageEnrollments() {
        System.out.println("\n--- Enrollment Management Menu ---");
        // Your enrollment management logic goes here
    }
    
    private void manageGrades() {
        System.out.println("\n--- Grades Management Menu ---");
        // Your grades management logic goes here
    }
    
    private void handleImportExport() {
        System.out.println("\n--- Import/Export Menu ---");
        // Your import/export logic goes here
    }

    private void handleBackupOperations() {
        System.out.println("\n--- Backup Operations Menu ---");
        // Your backup logic goes here
    }

    private void generateReports() {
        System.out.println("\n--- Reports Menu ---");
        // Your reports logic goes here
    }

    private void displayJavaInfo() {
        System.out.println("\n--- Java Platform Information ---");
        // Your Java info display logic goes here
    }

    // --- Getters for Services (used in CLIInterface.java) ---
    public StudentService getStudentService() {
        return studentService;
    }

    public CourseService getCourseService() {
        return courseService;
    }
}