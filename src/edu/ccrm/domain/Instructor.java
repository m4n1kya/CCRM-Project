package edu.ccrm.domain;

public class Instructor extends Person {
    private String department;
    private String facultyId;
    
    public Instructor(String id, String fullName, String email, String department, String facultyId) {
        super(id, fullName, email);
        this.department = department;
        this.facultyId = facultyId;
    }
    
    public String getDepartment() { return department; }
    public String getFacultyId() { return facultyId; }
    
    @Override
    public void displayProfile() {
        System.out.println("Instructor Profile:");
        System.out.println("ID: " + getId());
        System.out.println("Faculty ID: " + facultyId);
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Department: " + department);
    }
    
    @Override
    public String getType() {
        return "Instructor";
    }
    
    @Override
    public String toString() {
        return String.format("Instructor{id='%s', facultyId='%s', name='%s', department='%s'}", 
                           getId(), facultyId, getFullName(), department);
    }
}