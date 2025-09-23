package edu.ccrm.io;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class BackupService {
    private final Path backupDirectory;

    public BackupService(Path backupDirectory) {
        this.backupDirectory = backupDirectory;
        try {
            if (!Files.exists(backupDirectory)) {
                Files.createDirectories(backupDirectory);
            }
        } catch (IOException e) {
            System.err.println("Failed to create backup directory: " + e.getMessage());
        }
    }
    
    // Add other methods here
    // public void createBackup(...)
    // public void showBackupDirectorySize()
}