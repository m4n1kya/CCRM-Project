package edu.ccrm.config;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;
    private List<Student> students;
    private List<Course> courses;
    private List<Instructor> instructors;
    
    private DataStore() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
        instructors = new ArrayList<>();
        initializeSampleData();
    }
    
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }
    
    private void initializeSampleData() {
        // Create sample instructors
        Instructor instructor1 = new Instructor("I001", "Dr. Smith", "smith@university.edu", "Computer Science", "F001");
        Instructor instructor2 = new Instructor("I002", "Prof. Johnson", "johnson@university.edu", "Mathematics", "F002");
        
        instructors.add(instructor1);
        instructors.add(instructor2);
        
        // Create sample courses
        try {
            Course cs101 = new Course.Builder()
                    .code(new edu.ccrm.domain.CourseCode("CS101"))
                    .title("Introduction to Computer Science")
                    .credits(3)
                    .instructor(instructor1)
                    .department("Computer Science")
                    .semester(edu.ccrm.domain.Semester.FALL)
                    .build();
            
            Course math201 = new Course.Builder()
                    .code(new edu.ccrm.domain.CourseCode("MATH201"))
                    .title("Calculus I")
                    .credits(4)
                    .instructor(instructor2)
                    .department("Mathematics")
                    .semester(edu.ccrm.domain.Semester.SPRING)
                    .build();
            
            courses.add(cs101);
            courses.add(math201);
            
        } catch (Exception e) {
            System.err.println("Error initializing sample data: " + e.getMessage());
        }
    }
    
    public List<Student> getStudents() { return new ArrayList<>(students); }
    public List<Course> getCourses() { return new ArrayList<>(courses); }
    public List<Instructor> getInstructors() { return new ArrayList<>(instructors); }
    
    public void addStudent(Student student) {
        students.add(student);
    }
    
    public void addCourse(Course course) {
        courses.add(course);
    }
    
    public void addInstructor(Instructor instructor) {
        instructors.add(instructor);
    }
}