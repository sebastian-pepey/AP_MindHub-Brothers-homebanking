package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CustomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.mindhub.homebanking.dtos.ClientDTO;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @RequestMapping("/clients")
    public ResponseEntity<Object> getClients(){
        return clientService.getClients();}

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password){
        return clientService.register(firstName,lastName,email,password);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getConnectedClient(Authentication authentication) {
        return clientService.getConnectedClient(authentication);
    }

    @RequestMapping("/clients/{id}")
    public ResponseEntity<Object> getClient(@PathVariable long id){
        return clientService.getClient(id);
    }

    @RequestMapping("/validation")
    public boolean isAdmin(Authentication authentication) {
        return clientService.isAdmin(authentication);
    }

    @RequestMapping(value = "/changeAuthority", method = RequestMethod.PATCH)
    public ResponseEntity<Object> changeAuthority(@RequestParam String email) {
        return clientService.changeAuthority(email);
    }

    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> addAccount(Authentication authentication) {
        return clientService.addAccount(authentication);
    }
    @RequestMapping(value = "/clients/current/accounts")
    public ResponseEntity<Object> showAccounts(Authentication authentication) {
        return clientService.showAccounts(authentication);
    }

    @RequestMapping(value = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> addCards(Authentication authentication,
                                           @RequestParam String cardType,
                                           @RequestParam String cardColor) {
        return clientService.addCards(authentication,cardType,cardColor);
    }

    @RequestMapping(value = "/clients/current/cards")
    public ResponseEntity<Object> showCards(Authentication authentication) {
        return clientService.showCards(authentication);
    }
@Transactional
    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> makeTransactions(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam String amount,
            @RequestParam String description,
            Authentication authentication){
        return clientService.makeTransactions(fromAccountNumber,toAccountNumber,amount,description,authentication);
    }

}


