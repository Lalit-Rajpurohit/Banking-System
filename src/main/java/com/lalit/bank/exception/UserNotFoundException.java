package com.lalit.bank.exception;

/**
 * Exception thrown when a user cannot be found.
 *
 * @author Lalit
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long userId) {
        super(String.format("User not found with id: %d", userId));
    }
}
