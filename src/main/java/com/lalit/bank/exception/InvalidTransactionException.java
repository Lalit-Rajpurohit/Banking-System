package com.lalit.bank.exception;

/**
 * Exception thrown when a transaction is invalid.
 *
 * @author Lalit
 */
public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String message) {
        super(message);
    }
}
