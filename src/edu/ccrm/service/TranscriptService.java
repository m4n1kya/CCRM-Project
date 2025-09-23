package edu.ccrm.service;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Transcript;

public class TranscriptService {
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;

    // This constructor matches the call from CLIInterface
    public TranscriptService(EnrollmentService enrollmentService, StudentService studentService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
    }
    
    // public Transcript generateTranscript(String studentId) { ... }
    // ...
}