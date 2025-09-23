package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.exception.InvalidDataException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CSV Parser utility class for parsing CSV files into domain objects
 * Demonstrates Stream API, Exception handling, and functional programming
 */
public class CSVParser {
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Functional interface for parsing lines
    @FunctionalInterface
    public interface LineParser<T> {
        T parse(String[] fields) throws InvalidDataException;
    }
    
    /**
     * Generic method to parse CSV files using Streams
     */
    public static <T> List<T> parseCSVFile(Path filePath, LineParser<T> parser, boolean skipHeader) 
            throws IOException {
        
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filePath);
        }
        
        try (Stream<String> lines = Files.lines(filePath)) {
            Stream<String> dataLines = skipHeader ? lines.skip(1) : lines;
            
            return dataLines
                .map(line -> line.trim()) // Trim whitespace
                .filter(line -> !line.isEmpty()) // Filter empty lines
                .filter(line -> !line.startsWith("#")) // Filter comments
                .map(line -> line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1)) // Handle quoted commas
                .map(fields -> {
                    // Trim each field
                    for (int i = 0; i < fields.length; i++) {
                        fields[i] = fields[i].trim().replaceAll("^\"|\"$", "");
                    }
                    return fields;
                })
                .map(fields -> {
                    try {
                        return parser.parse(fields);
                    } catch (InvalidDataException e) {
                        System.err.println("Error parsing line: " + String.join(",", fields) + 
                                         " - " + e.getMessage());
                        return null;
                    }
                })
                .filter(obj -> obj != null) // Filter out failed parses
                .collect(Collectors.toList());
        }
    }
    
    /**
     * Parse students from CSV file
     */
    public static List<Student> parseStudentsFromCSV(Path filePath) throws IOException {
        return parseCSVFile(filePath, CSVParser::parseStudent, true);
    }
    
    /**
     * Parse courses from CSV file
     */
    public static List<Course> parseCoursesFromCSV(Path filePath) throws IOException {
        return parseCSVFile(filePath, CSVParser::parseCourse, true);
    }
    
    /**
     * Parse enrollments from CSV file
     */
    public static List<EnrollmentData> parseEnrollmentsFromCSV(Path filePath) throws IOException {
        return parseCSVFile(filePath, CSVParser::parseEnrollment, true);
    }
    
    /**
     * Parse a single student from CSV fields
     */
    private static Student parseStudent(String[] fields) throws InvalidDataException {
        if (fields.length < 5) {
            throw new InvalidDataException("Invalid student data: expected 5 fields, got " + fields.length);
        }
        
        try {
            String id = fields[0];
            String regNo = fields[1];
            String fullName = fields[2];
            String email = fields[3];
            boolean active = Boolean.parseBoolean(fields[4]);
            
            // Validate required fields
            if (id.isEmpty() || regNo.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                throw new InvalidDataException("Required student fields cannot be empty");
            }
            
            Student student = new Student.Builder()
                    .id(id)
                    .regNo(regNo)
                    .fullName(fullName)
                    .email(email)
                    .build();
            
            if (!active) {
                student.setActive(false);
            }
            
            return student;
            
        } catch (Exception e) {
            throw new InvalidDataException("Error parsing student: " + e.getMessage(), e);
        }
    }
    
    /**
     * Parse a single course from CSV fields
     */
    private static Course parseCourse(String[] fields) throws InvalidDataException {
        if (fields.length < 6) {
            throw new InvalidDataException("Invalid course data: expected 6 fields, got " + fields.length);
        }
        
        try {
            CourseCode code = new CourseCode(fields[0]);
            String title = fields[1];
            int credits = Integer.parseInt(fields[2]);
            String department = fields[3];
            Semester semester = Semester.valueOf(fields[4].toUpperCase());
            boolean active = Boolean.parseBoolean(fields[5]);
            
            // Validate credits
            if (credits <= 0 || credits > 6) {
                throw new InvalidDataException("Invalid credits: " + credits + ". Must be between 1 and 6.");
            }
            
            Course course = new Course.Builder()
                    .code(code)
                    .title(title)
                    .credits(credits)
                    .department(department)
                    .semester(semester)
                    .build();
            
            if (!active) {
                course.setActive(false);
            }
            
            return course;
            
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Error parsing course: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new InvalidDataException("Unexpected error parsing course", e);
        }
    }
    
    /**
     * Parse enrollment data from CSV (helper class for enrollment parsing)
     */
    private static EnrollmentData parseEnrollment(String[] fields) throws InvalidDataException {
        if (fields.length < 4) {
            throw new InvalidDataException("Invalid enrollment data: expected 4 fields, got " + fields.length);
        }
        
        try {
            String studentRegNo = fields[0];
            String courseCode = fields[1];
            LocalDateTime enrollmentDate = parseDateTime(fields[2]);
            Grade grade = parseGrade(fields[3]);
            
            return new EnrollmentData(studentRegNo, courseCode, enrollmentDate, grade);
            
        } catch (Exception e) {
            throw new InvalidDataException("Error parsing enrollment: " + e.getMessage(), e);
        }
    }
    
    /**
     * Utility method to parse date-time strings
     */
    private static LocalDateTime parseDateTime(String dateTimeStr) throws InvalidDataException {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return LocalDateTime.now();
        }
        
        try {
            return LocalDateTime.parse(dateTimeStr, DATE_FORMATTER);
        } catch (Exception e) {
            throw new InvalidDataException("Invalid date format: " + dateTimeStr + 
                                         ". Expected format: " + DATE_FORMATTER.toString());
        }
    }
    
    /**
     * Utility method to parse grade strings
     */
    private static Grade parseGrade(String gradeStr) throws InvalidDataException {
        if (gradeStr == null || gradeStr.isEmpty() || gradeStr.equalsIgnoreCase("NOT_GRADED")) {
            return Grade.NOT_GRADED;
        }
        
        try {
            return Grade.valueOf(gradeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Invalid grade: " + gradeStr + 
                                         ". Valid grades: " + java.util.Arrays.toString(Grade.values()));
        }
    }
    
    /**
     * Export students to CSV format
     */
    public static List<String> convertStudentsToCSV(List<Student> students) {
        List<String> lines = new ArrayList<>();
        
        // Add header
        lines.add("ID,RegNo,FullName,Email,Status,CreatedDate");
        
        // Convert each student to CSV line
        students.forEach(student -> {
            String line = String.join(",",
                escapeCsvField(student.getId()),
                escapeCsvField(student.getRegNo()),
                escapeCsvField(student.getFullName()),
                escapeCsvField(student.getEmail()),
                String.valueOf(student.isActive()),
                student.getCreatedDate().format(DATE_FORMATTER)
            );
            lines.add(line);
        });
        
        return lines;
    }
    
    /**
     * Export courses to CSV format
     */
    public static List<String> convertCoursesToCSV(List<Course> courses) {
        List<String> lines = new ArrayList<>();
        
        // Add header
        lines.add("Code,Title,Credits,Department,Semester,Status,Instructor");
        
        // Convert each course to CSV line
        courses.forEach(course -> {
            String instructorInfo = course.getInstructor() != null ? 
                course.getInstructor().getFacultyId() : "N/A";
            
            String line = String.join(",",
                escapeCsvField(course.getCode().toString()),
                escapeCsvField(course.getTitle()),
                String.valueOf(course.getCredits()),
                escapeCsvField(course.getDepartment()),
                course.getSemester().name(),
                String.valueOf(course.isActive()),
                escapeCsvField(instructorInfo)
            );
            lines.add(line);
        });
        
        return lines;
    }
    
    /**
     * Export enrollments to CSV format
     */
    public static List<String> convertEnrollmentsToCSV(List<Enrollment> enrollments) {
        List<String> lines = new ArrayList<>();
        
        // Add header
        lines.add("StudentRegNo,CourseCode,EnrollmentDate,Grade");
        
        // Convert each enrollment to CSV line
        enrollments.forEach(enrollment -> {
            String line = String.join(",",
                escapeCsvField(enrollment.getStudent().getRegNo()),
                escapeCsvField(enrollment.getCourse().getCode().toString()),
                enrollment.getEnrollmentDate().format(DATE_FORMATTER),
                enrollment.getGrade().name()
            );
            lines.add(line);
        });
        
        return lines;
    }
    
    /**
     * Escape CSV fields that contain commas or quotes
     */
    private static String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        
        // If field contains comma, quote, or newline, surround with quotes and escape existing quotes
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        
        return field;
    }
    
    /**
     * Validate CSV file structure before parsing
     */
    public static boolean validateCSVStructure(Path filePath, int expectedFields) throws IOException {
        if (!Files.exists(filePath)) {
            return false;
        }
        
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines
                .filter(line -> !line.trim().isEmpty())
                .filter(line -> !line.startsWith("#"))
                .map(line -> line.split(","))
                .allMatch(fields -> fields.length >= expectedFields);
        }
    }
    
    /**
     * Get CSV file statistics using Streams
     */
    public static void printCSVStatistics(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            System.out.println("File not found: " + filePath);
            return;
        }
        
        try (Stream<String> lines = Files.lines(filePath)) {
            long totalLines = lines.count();
            
            // Reopen stream for content analysis
            try (Stream<String> lines2 = Files.lines(filePath)) {
                long dataLines = lines2
                    .filter(line -> !line.trim().isEmpty())
                    .filter(line -> !line.startsWith("#"))
                    .count();
                
                System.out.println("CSV File Statistics:");
                System.out.println("  Total lines: " + totalLines);
                System.out.println("  Data lines: " + dataLines);
                System.out.println("  File size: " + Files.size(filePath) + " bytes");
            }
        }
    }
    
    /**
     * Helper class for enrollment data parsing
     */
    public static class EnrollmentData {
        private String studentRegNo;
        private String courseCode;
        private LocalDateTime enrollmentDate;
        private Grade grade;
        
        public EnrollmentData(String studentRegNo, String courseCode, 
                            LocalDateTime enrollmentDate, Grade grade) {
            this.studentRegNo = studentRegNo;
            this.courseCode = courseCode;
            this.enrollmentDate = enrollmentDate;
            this.grade = grade;
        }
        
        // Getters
        public String getStudentRegNo() { return studentRegNo; }
        public String getCourseCode() { return courseCode; }
        public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
        public Grade getGrade() { return grade; }
    }
    
    /**
     * Advanced CSV parsing with custom delimiters and error handling
     */
    public static class AdvancedCSVParser {
        private String delimiter;
        private boolean strictMode;
        
        public AdvancedCSVParser() {
            this(",", false);
        }
        
        public AdvancedCSVParser(String delimiter, boolean strictMode) {
            this.delimiter = delimiter;
            this.strictMode = strictMode;
        }
        
        public <T> List<T> parseFile(Path filePath, Function<String[], T> mapper) throws IOException {
            List<T> results = new ArrayList<>();
            int lineNumber = 0;
            
            try (Stream<String> lines = Files.lines(filePath)) {
                List<String> lineList = lines.collect(Collectors.toList());
                
                for (String line : lineList) {
                    lineNumber++;
                    
                    try {
                        if (line.trim().isEmpty() || line.startsWith("#")) {
                            continue; // Skip empty lines and comments
                        }
                        
                        // Handle quoted fields and custom delimiters
                        String[] fields = parseLineWithQuotes(line, delimiter);
                        
                        T result = mapper.apply(fields);
                        if (result != null) {
                            results.add(result);
                        }
                        
                    } catch (Exception e) {
                        if (strictMode) {
                            throw new IOException("Error parsing line " + lineNumber + ": " + e.getMessage(), e);
                        } else {
                            System.err.println("Warning: Error parsing line " + lineNumber + ": " + e.getMessage());
                        }
                    }
                }
            }
            
            return results;
        }
        
        /**
         * Parse CSV line handling quoted fields and escaped characters
         */
        private String[] parseLineWithQuotes(String line, String delimiter) {
            List<String> fields = new ArrayList<>();
            StringBuilder currentField = new StringBuilder();
            boolean inQuotes = false;
            boolean escapeNext = false;
            
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                
                if (escapeNext) {
                    currentField.append(c);
                    escapeNext = false;
                    continue;
                }
                
                if (c == '\\') {
                    escapeNext = true;
                    continue;
                }
                
                if (c == '"') {
                    inQuotes = !inQuotes;
                    continue;
                }
                
                if (!inQuotes && line.startsWith(delimiter, i)) {
                    fields.add(currentField.toString());
                    currentField.setLength(0);
                    i += delimiter.length() - 1;
                    continue;
                }
                
                currentField.append(c);
            }
            
            fields.add(currentField.toString());
            return fields.toArray(new String[0]);
        }
    }
    
    /**
     * Demonstrate various CSV parsing techniques
     */
    public static void demonstrateCSVParsing() {
        System.out.println("=== CSV Parsing Techniques ===");
        
        // Example of different parsing strategies
        String sampleLine = "John Doe,\"Smith, Jr.\",25,\"Software Engineer\"";
        
        System.out.println("Sample line: " + sampleLine);
        
        // Simple split (won't handle quoted commas correctly)
        String[] simpleSplit = sampleLine.split(",");
        System.out.println("Simple split: " + java.util.Arrays.toString(simpleSplit));
        
        // Advanced parsing with quote handling
        AdvancedCSVParser advancedParser = new AdvancedCSVParser();
        String[] advancedSplit = advancedParser.parseLineWithQuotes(sampleLine, ",");
        System.out.println("Advanced parse: " + java.util.Arrays.toString(advancedSplit));
    }
}