package com.lalit.bank.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lalit.bank.entity.Account;
import com.lalit.bank.entity.Transaction;

/**
 * Repository interface for Transaction entity operations.
 *
 * Provides standard CRUD operations plus custom query methods
 * for transaction history retrieval.
 *
 * @author Lalit
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions for an account (both incoming and outgoing).
     * Results are ordered by creation date descending (newest first).
     *
     * @param fromAccount The account to check as source
     * @param toAccount The account to check as destination
     * @return List of transactions involving the account
     */
    List<Transaction> findByFromAccountOrToAccountOrderByCreatedAtDesc(
            Account fromAccount,
            Account toAccount
    );

    /**
     * Finds all transactions for an account with pagination.
     * Results are ordered by creation date descending (newest first).
     *
     * @param fromAccount The account to check as source
     * @param toAccount The account to check as destination
     * @param pageable Pagination parameters
     * @return Page of transactions involving the account
     */
    Page<Transaction> findByFromAccountOrToAccountOrderByCreatedAtDesc(
            Account fromAccount,
            Account toAccount,
            Pageable pageable
    );

    /**
     * Finds all transactions for an account by account number with pagination.
     *
     * @param accountNumber The account number
     * @param pageable Pagination parameters
     * @return Page of transactions involving the account
     */
    @Query("SELECT t FROM Transaction t " +
           "WHERE t.fromAccount.accountNumber = :accountNumber " +
           "OR t.toAccount.accountNumber = :accountNumber " +
           "ORDER BY t.createdAt DESC")
    Page<Transaction> findByAccountNumber(
            @Param("accountNumber") String accountNumber,
            Pageable pageable
    );

    /**
     * Finds all transactions for an account by account ID.
     *
     * @param accountId The account ID
     * @return List of transactions involving the account
     */
    @Query("SELECT t FROM Transaction t " +
           "WHERE t.fromAccount.id = :accountId " +
           "OR t.toAccount.id = :accountId " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountId(@Param("accountId") Long accountId);
}
