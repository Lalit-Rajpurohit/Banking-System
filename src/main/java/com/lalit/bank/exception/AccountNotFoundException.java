package com.lalit.bank.exception;

/**
 * Exception thrown when an account cannot be found.
 *
 * @author Lalit
 */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String accountNumber, boolean byNumber) {
        super(byNumber
                ? String.format("Account not found with number: %s", accountNumber)
                : String.format("Account not found with id: %s", accountNumber));
    }
}
