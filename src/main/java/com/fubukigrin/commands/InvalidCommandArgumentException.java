package com.fubukigrin.commands;

/**
 * This exception class is mainly for the bot to return 
 * funny messages and stuff along the line lol. Maybe it can 
 * be implemented later? :3c
 */
public class InvalidCommandArgumentException extends Exception {
    private String errorMessage;

    public InvalidCommandArgumentException(String message) {
        super(message);
        errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
