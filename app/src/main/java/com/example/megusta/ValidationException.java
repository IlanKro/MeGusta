package com.example.megusta;

public class ValidationException extends Exception {
    /*
    An exceptions that handles validation messages in the app.
     */
    public ValidationException(String error_msg) {
        super(error_msg);
    }
}
