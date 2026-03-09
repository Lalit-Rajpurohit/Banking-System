package com.lalit.bank;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.lalit.bank.dto.account.AccountResponse;
import com.lalit.bank.dto.auth.AuthResponse;
import com.lalit.bank.dto.auth.RegisterRequest;
import com.lalit.bank.dto.transaction.TransactionResponse;
import com.lalit.bank.entity.Account.AccountType;
import com.lalit.bank.entity.Transaction.TransactionStatus;
import com.lalit.bank.entity.Transaction.TransactionType;
import com.lalit.bank.exception.InsufficientFundsException;
import com.lalit.bank.service.AccountService;
import com.lalit.bank.service.TransactionService;
import com.lalit.bank.service.UserService;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TransactionIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    private AuthResponse user1;
    private AuthResponse user2;
    private AccountResponse account1;
    private AccountResponse account2;

    @BeforeEach
    void setUp() {
        RegisterRequest register1 = new RegisterRequest("John Doe", "john@example.com", "password123");
        user1 = userService.register(register1);

        RegisterRequest register2 = new RegisterRequest("Jane Smith", "jane@example.com", "password456");
        user2 = userService.register(register2);

        account1 = accountService.createAccount(user1.getUser().getId(), AccountType.CHECKING);
        account2 = accountService.createAccount(user2.getUser().getId(), AccountType.SAVINGS);
    }

    @Test
    @DisplayName("Should successfully transfer funds between accounts")
    void testSuccessfulTransfer() {
        BigDecimal initialDeposit = new BigDecimal("1000.00");
        BigDecimal transferAmount = new BigDecimal("250.00");

        accountService.deposit(account1.getAccountNumber(), initialDeposit, "Initial deposit");

        TransactionResponse transaction = transactionService.transfer(
                account1.getAccountNumber(), account2.getAccountNumber(), transferAmount, "Test transfer");

        assertNotNull(transaction);
        assertNotNull(transaction.getId());
        assertEquals(TransactionType.TRANSFER, transaction.getType());
        assertEquals(TransactionStatus.SUCCESS, transaction.getStatus());
        assertEquals(account1.getAccountNumber(), transaction.getFromAccountNumber());
        assertEquals(account2.getAccountNumber(), transaction.getToAccountNumber());
        assertEquals(0, transferAmount.compareTo(transaction.getAmount()));

        AccountResponse updatedAccount1 = accountService.getAccountDetails(account1.getAccountNumber());
        AccountResponse updatedAccount2 = accountService.getAccountDetails(account2.getAccountNumber());

        BigDecimal expectedBalance1 = initialDeposit.subtract(transferAmount);
        BigDecimal expectedBalance2 = transferAmount;

        assertEquals(0, expectedBalance1.compareTo(updatedAccount1.getBalance()));
        assertEquals(0, expectedBalance2.compareTo(updatedAccount2.getBalance()));
    }

    @Test
    @DisplayName("Should fail transfer when insufficient funds")
    void testInsufficientFundsTransfer() {
        BigDecimal transferAmount = new BigDecimal("100.00");

        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class,
                () -> transactionService.transfer(account1.getAccountNumber(), account2.getAccountNumber(),
                        transferAmount, "Should fail"));

        assertTrue(exception.getMessage().contains("Insufficient funds"));

        AccountResponse updatedAccount1 = accountService.getAccountDetails(account1.getAccountNumber());
        AccountResponse updatedAccount2 = accountService.getAccountDetails(account2.getAccountNumber());

        assertEquals(0, BigDecimal.ZERO.compareTo(updatedAccount1.getBalance()));
        assertEquals(0, BigDecimal.ZERO.compareTo(updatedAccount2.getBalance()));
    }

    @Test
    @DisplayName("Should correctly handle deposit and withdrawal sequence")
    void testDepositAndWithdrawal() {
        BigDecimal depositAmount = new BigDecimal("500.00");
        BigDecimal withdrawAmount = new BigDecimal("200.00");

        AccountResponse afterDeposit = accountService.deposit(account1.getAccountNumber(), depositAmount, "Test deposit");
        assertEquals(0, depositAmount.compareTo(afterDeposit.getBalance()));

        AccountResponse afterWithdraw = accountService.withdraw(account1.getAccountNumber(), withdrawAmount, "Test withdrawal");
        BigDecimal expectedBalance = depositAmount.subtract(withdrawAmount);
        assertEquals(0, expectedBalance.compareTo(afterWithdraw.getBalance()));
    }

    @Test
    @DisplayName("Should maintain atomicity on multiple sequential transfers")
    void testMultipleTransfers() {
        BigDecimal initialBalance = new BigDecimal("1000.00");
        accountService.deposit(account1.getAccountNumber(), initialBalance, "Initial");

        transactionService.transfer(account1.getAccountNumber(), account2.getAccountNumber(), new BigDecimal("100.00"), "Transfer 1");
        transactionService.transfer(account1.getAccountNumber(), account2.getAccountNumber(), new BigDecimal("150.00"), "Transfer 2");
        transactionService.transfer(account2.getAccountNumber(), account1.getAccountNumber(), new BigDecimal("50.00"), "Transfer back");

        AccountResponse finalAccount1 = accountService.getAccountDetails(account1.getAccountNumber());
        AccountResponse finalAccount2 = accountService.getAccountDetails(account2.getAccountNumber());

        assertEquals(0, new BigDecimal("800.00").compareTo(finalAccount1.getBalance()));
        assertEquals(0, new BigDecimal("200.00").compareTo(finalAccount2.getBalance()));
    }
}
