package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Person {
    protected String id;
    protected String fullName;
    protected String email;
    protected boolean active;
    protected LocalDateTime createdDate;
    
    public Person(String id, String fullName, String email) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.fullName = Objects.requireNonNull(fullName, "Full name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.active = true;
        this.createdDate = LocalDateTime.now();
    }
    
    // Abstract methods
    public abstract void displayProfile();
    public abstract String getType();
    
    // Getters and setters
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    
    @Override
    public String toString() {
        return String.format("Person{id='%s', name='%s', email='%s', active=%s}", 
                           id, fullName, email, active);
    }
}