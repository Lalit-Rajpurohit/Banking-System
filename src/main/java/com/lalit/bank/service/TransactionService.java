package com.lalit.bank.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.lalit.bank.dto.transaction.TransactionResponse;
import com.lalit.bank.entity.Account;
import com.lalit.bank.entity.Transaction;
import com.lalit.bank.entity.Transaction.TransactionStatus;
import com.lalit.bank.entity.Transaction.TransactionType;
import com.lalit.bank.exception.AccountNotFoundException;
import com.lalit.bank.exception.InsufficientFundsException;
import com.lalit.bank.exception.InvalidTransactionException;
import com.lalit.bank.repository.AccountRepository;
import com.lalit.bank.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TransactionResponse transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new InvalidTransactionException("Cannot transfer to the same account");
        }

        Account fromAccount;
        Account toAccount;

        if (fromAccountNumber.compareTo(toAccountNumber) < 0) {
            fromAccount = getAccountWithLock(fromAccountNumber);
            toAccount = getAccountWithLock(toAccountNumber);
        } else {
            toAccount = getAccountWithLock(toAccountNumber);
            fromAccount = getAccountWithLock(fromAccountNumber);
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            Transaction failedTransaction = Transaction.builder()
                    .fromAccount(fromAccount)
                    .toAccount(toAccount)
                    .amount(amount)
                    .type(TransactionType.TRANSFER)
                    .status(TransactionStatus.FAILED)
                    .description("Failed: Insufficient funds")
                    .build();
            transactionRepository.save(failedTransaction);
            throw new InsufficientFundsException(fromAccountNumber, amount.toString());
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(amount)
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .description(description != null ? description : "Transfer")
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionResponse.fromEntity(savedTransaction);
    }

    public Page<TransactionResponse> getHistory(String accountNumber, Pageable pageable) {
        return transactionRepository.findByAccountNumber(accountNumber, pageable)
                .map(TransactionResponse::fromEntity);
    }

    private Account getAccountWithLock(String accountNumber) {
        try {
            return accountRepository.findByAccountNumberWithLock(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException(accountNumber, true));
        } catch (Exception e) {
            return accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException(accountNumber, true));
        }
    }
}
