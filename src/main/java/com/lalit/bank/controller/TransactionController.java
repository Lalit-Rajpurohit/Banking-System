package com.lalit.bank.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lalit.bank.dto.account.AccountResponse;
import com.lalit.bank.dto.transaction.DepositRequest;
import com.lalit.bank.dto.transaction.TransactionResponse;
import com.lalit.bank.dto.transaction.TransferRequest;
import com.lalit.bank.dto.transaction.WithdrawRequest;
import com.lalit.bank.service.AccountService;
import com.lalit.bank.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountResponse> deposit(@Valid @RequestBody DepositRequest request) {
        AccountResponse response = accountService.deposit(request.getAccountNumber(), request.getAmount(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        AccountResponse response = accountService.withdraw(request.getAccountNumber(), request.getAmount(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        TransactionResponse response = transactionService.transfer(
                request.getFromAccountNumber(), request.getToAccountNumber(), request.getAmount(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<Page<TransactionResponse>> getHistory(@PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionResponse> history = transactionService.getHistory(accountNumber, pageable);
        return ResponseEntity.ok(history);
    }
}
