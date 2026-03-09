package com.lalit.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lalit.bank.entity.User;

/**
 * Repository interface for User entity operations.
 *
 * Provides standard CRUD operations plus custom query methods
 * for user lookup by email.
 *
 * @author Lalit
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email address to check
     * @return true if a user with this email exists
     */
    boolean existsByEmail(String email);
}
