package edu.ccrm.service;

public interface Persistable {
    void saveToFile(String filename);
    void loadFromFile(String filename);
    
    default String getFileFormat() {
        return "CSV";
    }
}