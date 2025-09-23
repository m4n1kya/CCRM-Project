package edu.ccrm.cli;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import edu.ccrm.config.AppConfig;
import edu.ccrm.io.BackupService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.TranscriptService;

public class CLIInterface {

    private final Scanner scanner;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final TranscriptService transcriptService;
    private final ImportExportService importExportService;
    private final BackupService backupService;
    private final MenuHandler menuHandler;
    private final AppConfig config;

    public CLIInterface() {
        this.config = AppConfig.getInstance();
        this.scanner = new Scanner(System.in);

        // Initialize all services
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.enrollmentService = new EnrollmentService();
        this.transcriptService = new TranscriptService(enrollmentService, studentService);
        this.importExportService = new ImportExportService(studentService, courseService, enrollmentService);

        // The BackupService constructor needs a Path
        Path backupPath = Paths.get(config.getBackupDirectory());
        this.backupService = new BackupService(backupPath);

        // Pass all dependencies to the MenuHandler
        this.menuHandler = new MenuHandler(
                scanner, studentService, courseService, enrollmentService,
                transcriptService, importExportService, backupService
        );

        initializeSampleData();
    }

    public static void main(String[] args) {
        CLIInterface app = new CLIInterface();
        app.start();
    }

    public void start() {
        System.out.println("Welcome to Campus Course & Records Manager!");
        
        int choice;
        do {
            displayMainMenu();
            choice = getIntInput("Enter your choice: ");
            menuHandler.handleMainMenu(choice);
        } while (choice != 0);

        System.out.println("Thank you for using CCRM. Goodbye!");
        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Courses");
        System.out.println("3. Manage Enrollments");
        System.out.println("4. Manage Grades");
        System.out.println("5. Import/Export Data");
        System.out.println("6. Backup Operations");
        System.out.println("7. Generate Reports");
        System.out.println("8. Display Java Platform Info");
        System.out.println("0. Exit");
        System.out.println("=================");
    }
    
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.next(); // Consume the invalid input
            System.out.print(prompt);
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume the rest of the line
        return input;
    }

    private void initializeSampleData() {
        // ... (This method remains the same)
    }
}