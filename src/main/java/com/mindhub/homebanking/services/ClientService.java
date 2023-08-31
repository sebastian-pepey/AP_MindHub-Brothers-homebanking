package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

public interface ClientService {
    void changeAuthority(@RequestParam String email);

    ResponseEntity<Object> getClients();

    ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password);

    ClientDTO getClient(@PathVariable long id);

    ClientDTO getConnectedClient(Authentication authentication);

    boolean isAdmin(Authentication auth);
    ResponseEntity<Object> addAccount(Authentication authentication);

    Set<AccountDTO> showAccounts(Authentication authentication);

    ResponseEntity<Object> addCards(Authentication authentication,
                                           @RequestParam String cardType,
                                           @RequestParam String cardColor);

    ResponseEntity<Object> showCards(Authentication authentication);

    ResponseEntity<Object> makeTransactions(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam String amount,
            @RequestParam String description,
            Authentication authentication);
}
