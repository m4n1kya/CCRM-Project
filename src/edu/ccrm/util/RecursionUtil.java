package edu.ccrm.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RecursionUtil {
    
    // Recursive method to find all files with specific extension
    public static List<Path> findFilesByExtension(Path directory, String extension) throws IOException {
        List<Path> result = new ArrayList<>();
        findFilesByExtensionRecursive(directory, extension, result);
        return result;
    }
    
    private static void findFilesByExtensionRecursive(Path directory, String extension, List<Path> result) 
            throws IOException {
        if (!Files.isDirectory(directory)) {
            return;
        }
        
        try (var paths = Files.list(directory)) {
            paths.forEach(path -> {
                if (Files.isDirectory(path)) {
                    try {
                        findFilesByExtensionRecursive(path, extension, result);
                    } catch (IOException e) {
                        System.err.println("Error accessing directory: " + path);
                    }
                } else if (path.toString().toLowerCase().endsWith(extension.toLowerCase())) {
                    result.add(path);
                }
            });
        }
    }
    
    // Recursive factorial for demonstration
    public static int factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number must be non-negative");
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }
    
    // Recursive Fibonacci sequence
    public static int fibonacci(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}