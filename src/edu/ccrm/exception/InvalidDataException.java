package edu.ccrm.exception;

/**
 * Custom exception for invalid data encountered during CSV parsing
 */
public class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
    
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public InvalidDataException(Throwable cause) {
        super(cause);
    }
}