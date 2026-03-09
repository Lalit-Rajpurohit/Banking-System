package com.lalit.bank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Account entity representing a bank account.
 */
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL)
    private List<Transaction> outgoingTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL)
    private List<Transaction> incomingTransactions = new ArrayList<>();

    public Account() {}

    public Account(Long id, String accountNumber, BigDecimal balance, AccountType accountType, User user, LocalDateTime createdAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance != null ? balance : BigDecimal.ZERO;
        this.accountType = accountType;
        this.user = user;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<Transaction> getOutgoingTransactions() { return outgoingTransactions; }
    public void setOutgoingTransactions(List<Transaction> outgoingTransactions) { this.outgoingTransactions = outgoingTransactions; }
    public List<Transaction> getIncomingTransactions() { return incomingTransactions; }
    public void setIncomingTransactions(List<Transaction> incomingTransactions) { this.incomingTransactions = incomingTransactions; }

    public static AccountBuilder builder() { return new AccountBuilder(); }

    public static class AccountBuilder {
        private Long id;
        private String accountNumber;
        private BigDecimal balance = BigDecimal.ZERO;
        private AccountType accountType;
        private User user;
        private LocalDateTime createdAt;

        public AccountBuilder id(Long id) { this.id = id; return this; }
        public AccountBuilder accountNumber(String accountNumber) { this.accountNumber = accountNumber; return this; }
        public AccountBuilder balance(BigDecimal balance) { this.balance = balance; return this; }
        public AccountBuilder accountType(AccountType accountType) { this.accountType = accountType; return this; }
        public AccountBuilder user(User user) { this.user = user; return this; }
        public AccountBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Account build() { return new Account(id, accountNumber, balance, accountType, user, createdAt); }
    }

    public enum AccountType { SAVINGS, CHECKING, BUSINESS }
}
