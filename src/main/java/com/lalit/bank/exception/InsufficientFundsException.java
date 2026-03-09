package com.lalit.bank.exception;

/**
 * Exception thrown when an account has insufficient funds for a transaction.
 *
 * @author Lalit
 */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(String accountNumber, String amount) {
        super(String.format("Insufficient funds in account %s for amount %s", accountNumber, amount));
    }
}
