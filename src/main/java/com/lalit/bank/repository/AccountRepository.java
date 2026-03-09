package com.lalit.bank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lalit.bank.entity.Account;

import jakarta.persistence.LockModeType;

/**
 * Repository interface for Account entity operations.
 *
 * Provides standard CRUD operations plus custom query methods
 * for account lookup and locking for transactional safety.
 *
 * @author Lalit
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Finds an account by its unique account number.
     *
     * @param accountNumber The account number to search for
     * @return Optional containing the account if found
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Finds an account by account number with a pessimistic write lock.
     * Used for transactional operations like transfers to prevent race conditions.
     *
     * Note: SQLite has limited lock support. This will work as a simple row lock
     * when supported, otherwise the application layer handles consistency.
     *
     * @param accountNumber The account number to search for
     * @return Optional containing the account if found
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumberWithLock(@Param("accountNumber") String accountNumber);

    /**
     * Finds all accounts belonging to a specific user.
     *
     * @param userId The user's ID
     * @return List of accounts owned by the user
     */
    List<Account> findByUserId(Long userId);

    /**
     * Finds all accounts belonging to a specific user by their email.
     *
     * @param email The user's email address
     * @return List of accounts owned by the user
     */
    @Query("SELECT a FROM Account a WHERE a.user.email = :email")
    List<Account> findByUserEmail(@Param("email") String email);

    /**
     * Checks if an account with the given account number exists.
     *
     * @param accountNumber The account number to check
     * @return true if an account with this number exists
     */
    boolean existsByAccountNumber(String accountNumber);
}
