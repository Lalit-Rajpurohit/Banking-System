package com.lalit.bank.dto.account;

import com.lalit.bank.entity.Account.AccountType;
import jakarta.validation.constraints.NotNull;

public class CreateAccountRequest {
    @NotNull(message = "Account type is required")
    private AccountType accountType;

    public CreateAccountRequest() {}
    public CreateAccountRequest(AccountType accountType) { this.accountType = accountType; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
}
