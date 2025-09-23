package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.CourseCode;
import edu.ccrm.domain.Semester;
import edu.ccrm.domain.Instructor;
import edu.ccrm.exception.DuplicateCourseException;
import edu.ccrm.exception.CourseNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CourseService implements Searchable<Course> {
    private List<Course> courses;
    
    public CourseService() {
        this.courses = new ArrayList<>();
    }
    
    public void addCourse(Course course) throws DuplicateCourseException {
        if (findCourseByCode(course.getCode()) != null) {
            throw new DuplicateCourseException("Course with code " + course.getCode() + " already exists");
        }
        courses.add(course);
    }
    
    public Course findCourseByCode(CourseCode code) {
        return courses.stream()
                .filter(c -> c.getCode().equals(code) && c.isActive())
                .findFirst()
                .orElse(null);
    }
    
    public List<Course> findCoursesByInstructor(Instructor instructor) {
        return courses.stream()
                .filter(c -> c.getInstructor() != null && 
                           c.getInstructor().equals(instructor) && c.isActive())
                .collect(Collectors.toList());
    }
    
    public List<Course> findCoursesBySemester(Semester semester) {
        return courses.stream()
                .filter(c -> c.getSemester() == semester && c.isActive())
                .collect(Collectors.toList());
    }
    
    public List<Course> findCoursesByDepartment(String department) {
        return courses.stream()
                .filter(c -> c.getDepartment() != null && 
                           c.getDepartment().equalsIgnoreCase(department) && c.isActive())
                .collect(Collectors.toList());
    }
    
    public void updateCourse(CourseCode code, String newTitle, int newCredits) throws CourseNotFoundException {
        Course course = findCourseByCode(code);
        if (course == null) {
            throw new CourseNotFoundException("Course with code " + code + " not found");
        }
        course.setTitle(newTitle);
        course.setCredits(newCredits);
    }
    
    public void deactivateCourse(CourseCode code) throws CourseNotFoundException {
        Course course = findCourseByCode(code);
        if (course == null) {
            throw new CourseNotFoundException("Course with code " + code + " not found");
        }
        course.setActive(false);
    }
    
    @Override
    public List<Course> search(Predicate<Course> predicate) {
        return courses.stream()
                .filter(predicate)
                .filter(Course::isActive)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> findAll() {
        return courses.stream()
                .filter(Course::isActive)
                .collect(Collectors.toList());
    }
}