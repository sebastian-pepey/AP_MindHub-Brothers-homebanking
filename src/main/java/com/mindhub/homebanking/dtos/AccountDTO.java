package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;

public class AccountDTO {
    private Long id;
    private String accountNumber;
    private LocalDate creationDate;
    private double accountBalance;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.accountBalance = account.getAccountBalance();
        this.creationDate = account.getCreationDate();
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getAccountBalance() {
        return accountBalance;
    }
}
