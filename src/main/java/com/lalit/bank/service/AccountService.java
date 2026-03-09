package com.lalit.bank.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lalit.bank.dto.account.AccountResponse;
import com.lalit.bank.entity.Account;
import com.lalit.bank.entity.Account.AccountType;
import com.lalit.bank.entity.Transaction;
import com.lalit.bank.entity.Transaction.TransactionStatus;
import com.lalit.bank.entity.Transaction.TransactionType;
import com.lalit.bank.entity.User;
import com.lalit.bank.exception.AccountNotFoundException;
import com.lalit.bank.exception.InsufficientFundsException;
import com.lalit.bank.repository.AccountRepository;
import com.lalit.bank.repository.TransactionRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Transactional
    public AccountResponse createAccount(Long userId, AccountType accountType) {
        User user = userService.getById(userId);
        String accountNumber = generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(accountType)
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        Account savedAccount = accountRepository.save(account);
        return AccountResponse.fromEntity(savedAccount);
    }

    @Transactional
    public AccountResponse createAccountByEmail(String userEmail, AccountType accountType) {
        User user = userService.getByEmail(userEmail);
        return createAccount(user.getId(), accountType);
    }

    public Account getByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber, true));
    }

    public AccountResponse getAccountDetails(String accountNumber) {
        Account account = getByAccountNumber(accountNumber);
        return AccountResponse.fromEntity(account);
    }

    public List<AccountResponse> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(AccountResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AccountResponse> getAccountsByUserEmail(String email) {
        return accountRepository.findByUserEmail(email).stream()
                .map(AccountResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountResponse deposit(String accountNumber, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Account account = getByAccountNumber(accountNumber);
        account.setBalance(account.getBalance().add(amount));
        Account savedAccount = accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .toAccount(account)
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.SUCCESS)
                .description(description != null ? description : "Deposit")
                .build();
        transactionRepository.save(transaction);

        return AccountResponse.fromEntity(savedAccount);
    }

    @Transactional
    public AccountResponse withdraw(String accountNumber, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Account account = getByAccountNumber(accountNumber);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(accountNumber, amount.toString());
        }

        account.setBalance(account.getBalance().subtract(amount));
        Account savedAccount = accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .fromAccount(account)
                .amount(amount)
                .type(TransactionType.WITHDRAW)
                .status(TransactionStatus.SUCCESS)
                .description(description != null ? description : "Withdrawal")
                .build();
        transactionRepository.save(transaction);

        return AccountResponse.fromEntity(savedAccount);
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = "ACC-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}
