package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<ClientLoanDTO> loans;
    private Set<AccountDTO> accounts;
    private Set<CardDTO> cards;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();

        // Se crea un Set (no está ordenado como una lista y no puede repetirse) de
        // * AccountDTO
        // * ClientDTO
        // * CardDTO
        // por medio del cual se formatean los objetos que formarán parte del JSON (transformador por Jackson)
        // devuelto por el controlador (del cual el servlet que forma parte el mismo).

        this.accounts = client.getAccounts().stream().map( account -> new AccountDTO(account)).collect(Collectors.toSet());
        this.loans = client.getLoans().stream().map( loan -> new ClientLoanDTO(loan)).collect(Collectors.toSet());
        this.cards = client.getCards().stream().map( card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
    public Set<ClientLoanDTO> getLoans() { return loans;}

    public Set<CardDTO> getCards() {
        return cards;
    }
}