package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface ClientService {
    ResponseEntity<Object> getClients();

    ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password);

    ClientDTO getConnectedClient(Authentication authentication);

    ResponseEntity<Object> getClient(@PathVariable long id);

    boolean isAdmin(Authentication auth);

   ResponseEntity<Object> changeAuthority(@RequestParam String email);

   ResponseEntity<Object> addAccount(Authentication authentication);
   ResponseEntity<Object> showAccounts(Authentication authentication);
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
