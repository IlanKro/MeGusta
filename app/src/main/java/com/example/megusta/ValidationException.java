package com.example.megusta;
/** Assignment: Concluding assignment
 * Campus: Ashdod
 * Author1: Ilan Kroter, ID: 323294843
 * Author2: Noy Nir, ID: 207993940
 **/
/**
    An exception that handles validation messages in the app.
 */
public class ValidationException extends Exception {
    /**
     * Creates an exception
     * @param error_msg the error message of the exception.
     */
    public ValidationException(String error_msg) {
        super(error_msg);
    }
}
