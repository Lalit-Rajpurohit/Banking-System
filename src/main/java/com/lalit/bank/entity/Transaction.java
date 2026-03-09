package com.lalit.bank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Transaction entity representing a banking transaction.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.SUCCESS;

    @Column(length = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Transaction() {}

    public Transaction(Long id, Account fromAccount, Account toAccount, BigDecimal amount, TransactionType type, TransactionStatus status, String description, LocalDateTime createdAt) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.status = status != null ? status : TransactionStatus.SUCCESS;
        this.description = description;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Account getFromAccount() { return fromAccount; }
    public void setFromAccount(Account fromAccount) { this.fromAccount = fromAccount; }
    public Account getToAccount() { return toAccount; }
    public void setToAccount(Account toAccount) { this.toAccount = toAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static TransactionBuilder builder() { return new TransactionBuilder(); }

    public static class TransactionBuilder {
        private Long id;
        private Account fromAccount;
        private Account toAccount;
        private BigDecimal amount;
        private TransactionType type;
        private TransactionStatus status = TransactionStatus.SUCCESS;
        private String description;
        private LocalDateTime createdAt;

        public TransactionBuilder id(Long id) { this.id = id; return this; }
        public TransactionBuilder fromAccount(Account fromAccount) { this.fromAccount = fromAccount; return this; }
        public TransactionBuilder toAccount(Account toAccount) { this.toAccount = toAccount; return this; }
        public TransactionBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public TransactionBuilder type(TransactionType type) { this.type = type; return this; }
        public TransactionBuilder status(TransactionStatus status) { this.status = status; return this; }
        public TransactionBuilder description(String description) { this.description = description; return this; }
        public TransactionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Transaction build() { return new Transaction(id, fromAccount, toAccount, amount, type, status, description, createdAt); }
    }

    public enum TransactionType { DEPOSIT, WITHDRAW, TRANSFER }
    public enum TransactionStatus { SUCCESS, FAILED, PENDING }
}
