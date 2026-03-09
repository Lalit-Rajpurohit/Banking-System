package com.lalit.bank.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.lalit.bank.entity.Transaction;
import com.lalit.bank.entity.Transaction.TransactionStatus;
import com.lalit.bank.entity.Transaction.TransactionType;

public class TransactionResponse {
    private Long id;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;

    public TransactionResponse() {}
    public TransactionResponse(Long id, String fromAccountNumber, String toAccountNumber, BigDecimal amount, TransactionType type, TransactionStatus status, String description, LocalDateTime createdAt) {
        this.id = id; this.fromAccountNumber = fromAccountNumber; this.toAccountNumber = toAccountNumber;
        this.amount = amount; this.type = type; this.status = status; this.description = description; this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFromAccountNumber() { return fromAccountNumber; }
    public void setFromAccountNumber(String fromAccountNumber) { this.fromAccountNumber = fromAccountNumber; }
    public String getToAccountNumber() { return toAccountNumber; }
    public void setToAccountNumber(String toAccountNumber) { this.toAccountNumber = toAccountNumber; }
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

    public static TransactionResponse fromEntity(Transaction transaction) {
        return builder()
            .id(transaction.getId())
            .fromAccountNumber(transaction.getFromAccount() != null ? transaction.getFromAccount().getAccountNumber() : null)
            .toAccountNumber(transaction.getToAccount() != null ? transaction.getToAccount().getAccountNumber() : null)
            .amount(transaction.getAmount())
            .type(transaction.getType())
            .status(transaction.getStatus())
            .description(transaction.getDescription())
            .createdAt(transaction.getCreatedAt())
            .build();
    }

    public static TransactionResponseBuilder builder() { return new TransactionResponseBuilder(); }
    public static class TransactionResponseBuilder {
        private Long id;
        private String fromAccountNumber, toAccountNumber, description;
        private BigDecimal amount;
        private TransactionType type;
        private TransactionStatus status;
        private LocalDateTime createdAt;
        public TransactionResponseBuilder id(Long id) { this.id = id; return this; }
        public TransactionResponseBuilder fromAccountNumber(String fromAccountNumber) { this.fromAccountNumber = fromAccountNumber; return this; }
        public TransactionResponseBuilder toAccountNumber(String toAccountNumber) { this.toAccountNumber = toAccountNumber; return this; }
        public TransactionResponseBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public TransactionResponseBuilder type(TransactionType type) { this.type = type; return this; }
        public TransactionResponseBuilder status(TransactionStatus status) { this.status = status; return this; }
        public TransactionResponseBuilder description(String description) { this.description = description; return this; }
        public TransactionResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public TransactionResponse build() { return new TransactionResponse(id, fromAccountNumber, toAccountNumber, amount, type, status, description, createdAt); }
    }
}
