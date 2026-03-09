package com.lalit.bank.dto.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.lalit.bank.entity.Account;
import com.lalit.bank.entity.Account.AccountType;

public class AccountResponse {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private Long userId;
    private String ownerName;
    private LocalDateTime createdAt;

    public AccountResponse() {}
    public AccountResponse(Long id, String accountNumber, BigDecimal balance, AccountType accountType, Long userId, String ownerName, LocalDateTime createdAt) {
        this.id = id; this.accountNumber = accountNumber; this.balance = balance;
        this.accountType = accountType; this.userId = userId; this.ownerName = ownerName; this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static AccountResponse fromEntity(Account account) {
        return builder()
            .id(account.getId())
            .accountNumber(account.getAccountNumber())
            .balance(account.getBalance())
            .accountType(account.getAccountType())
            .userId(account.getUser().getId())
            .ownerName(account.getUser().getName())
            .createdAt(account.getCreatedAt())
            .build();
    }

    public static AccountResponseBuilder builder() { return new AccountResponseBuilder(); }
    public static class AccountResponseBuilder {
        private Long id, userId;
        private String accountNumber, ownerName;
        private BigDecimal balance;
        private AccountType accountType;
        private LocalDateTime createdAt;
        public AccountResponseBuilder id(Long id) { this.id = id; return this; }
        public AccountResponseBuilder accountNumber(String accountNumber) { this.accountNumber = accountNumber; return this; }
        public AccountResponseBuilder balance(BigDecimal balance) { this.balance = balance; return this; }
        public AccountResponseBuilder accountType(AccountType accountType) { this.accountType = accountType; return this; }
        public AccountResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public AccountResponseBuilder ownerName(String ownerName) { this.ownerName = ownerName; return this; }
        public AccountResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AccountResponse build() { return new AccountResponse(id, accountNumber, balance, accountType, userId, ownerName, createdAt); }
    }
}
