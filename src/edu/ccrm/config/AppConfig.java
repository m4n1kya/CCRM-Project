package edu.ccrm.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class AppConfig {
    private static AppConfig instance;
    private Properties properties;
    private String dataDirectory;
    private String backupDirectory;
    
    private AppConfig() {
        // Private constructor for singleton
        properties = new Properties();
        loadDefaultProperties();
    }
    
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
    
    private void loadDefaultProperties() {
        properties.setProperty("data.directory", "data");
        properties.setProperty("backup.directory", "backups");
        properties.setProperty("max.credits.per.semester", "18");
        properties.setProperty("date.format", "yyyy-MM-dd HH:mm:ss");
    }
    
    public void loadConfiguration() {
        try {
            Path configFile = Paths.get("config.properties");
            if (Files.exists(configFile)) {
                properties.load(Files.newInputStream(configFile));
            }
            
            // Create directories if they don't exist
            dataDirectory = properties.getProperty("data.directory");
            backupDirectory = properties.getProperty("backup.directory");
            
            Files.createDirectories(Paths.get(dataDirectory));
            Files.createDirectories(Paths.get(backupDirectory));
            
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }
    
    public String getDataDirectory() { return dataDirectory; }
    public String getBackupDirectory() { return backupDirectory; }
    public int getMaxCreditsPerSemester() { 
        return Integer.parseInt(properties.getProperty("max.credits.per.semester")); 
    }
    public DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(properties.getProperty("date.format"));
    }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton cannot be cloned");
    }
}