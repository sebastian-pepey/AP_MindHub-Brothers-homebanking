package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private Long id;
    private String accountNumber;
    private LocalDateTime creationDate;
    private double accountBalance;

    private Set<TransactionDTO> transactions;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.accountBalance = account.getAccountBalance();
        this.creationDate = account.getCreationDate();
        this.transactions = account.getTransactions().stream().map( transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}
