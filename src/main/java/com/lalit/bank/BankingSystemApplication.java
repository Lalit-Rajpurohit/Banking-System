package com.lalit.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.lalit.bank.config.ApplicationProperties;

/**
 * Main entry point for the Banking System application.
 *
 * This application provides a REST API for banking operations including:
 * - User registration and authentication (JWT-based)
 * - Account management (create, view accounts)
 * - Transaction operations (deposit, withdraw, transfer)
 *
 * @author Lalit
 * @version 1.0
 */
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class BankingSystemApplication {


    public static void main(String[] args) {
        SpringApplication.run(BankingSystemApplication.class, args);
    }
}
