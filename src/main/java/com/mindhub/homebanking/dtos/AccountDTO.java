package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Los Data Transfer Objects se emplean para restringir la información que se trae desde la DB.

public class AccountDTO {
    private Long id;
    private String number;
    private LocalDate date;
    private double balance;
    private List<TransactionType> transactionTypes;

    private Set<TransactionDTO> transactions;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getAccountNumber();
        this.balance = account.getAccountBalance();
        this.date = account.getCreationDate();
        this.transactions = account.getTransactions().stream().map( transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
        this.transactionTypes = Arrays.asList(TransactionType.values());
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getdate() {
        return date;
    }

    public double getbalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public List<TransactionType> getTransactionTypes() {
        return transactionTypes;
    }
}
