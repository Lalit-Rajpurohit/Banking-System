package com.lalit.bank.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lalit.bank.dto.account.AccountResponse;
import com.lalit.bank.dto.account.CreateAccountRequest;
import com.lalit.bank.entity.User;
import com.lalit.bank.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request,
            @AuthenticationPrincipal User user) {
        AccountResponse response = accountService.createAccount(user.getId(), request.getAccountType());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getMyAccounts(@AuthenticationPrincipal User user) {
        List<AccountResponse> accounts = accountService.getAccountsByUserId(user.getId());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber) {
        AccountResponse response = accountService.getAccountDetails(accountNumber);
        return ResponseEntity.ok(response);
    }
}
