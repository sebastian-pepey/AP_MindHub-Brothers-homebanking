package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password){
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }

        if(clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        clientRepository.save(new Client(firstName,lastName,email,passwordEncoder.encode(password), ClientAuthority.CLIENT));
        return new ResponseEntity<>("Client created",HttpStatus.CREATED);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getConnectedClient(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> addAccount(Authentication authentication) {

        Client currentClient = clientRepository.findByEmail(authentication.getName());
        if(currentClient.getAccounts().size() < 3 ){
            Account newAccount = new Account();
            // Set attributes of new Account
            newAccount.setCreationDate(LocalDate.now());
            newAccount.setAccountNumber("VIN"+LocalDate.now().toString());
            newAccount.setAccountBalance(0);

            System.out.println(newAccount);

            currentClient.addAccount(newAccount);

            accountRepository.save(newAccount);

            clientRepository.save(currentClient);

            return new ResponseEntity<>("Account added",HttpStatus.OK);

        } else {
            return new ResponseEntity<>("The client can't create more than 3 accounts",HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/clients/current/accounts")
    public Set<AccountDTO> showAccounts(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).getAccounts().stream().map( account -> new AccountDTO(account)).collect(Collectors.toSet());
    }

    @RequestMapping(value = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> addCards(Authentication authentication,
                                           @RequestParam String cardType,
                                           @RequestParam String cardColor) {

        Client currentClient = clientRepository.findByEmail(authentication.getName());
        if(currentClient.getCards().size() < 3 ){
            Card newCard = new Card();
            // Set attributes of new Account
            newCard.setCardholder(currentClient);
            newCard.setCardColor(CardColor.valueOf(cardColor));
            newCard.setNumber("4444 6745 7876 4445");
            newCard.setFromDate(LocalDate.of(2021,04,26));
            newCard.setThruDate(LocalDate.of(2021,04,26).plusYears(5));
            newCard.setCardType(CardType.valueOf(cardType));
            newCard.setCvv(110);
            currentClient.addCard(newCard);
            cardRepository.save(newCard);
            clientRepository.save(currentClient);
            return new ResponseEntity<>("Card Created",HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("The client can't create more than 3 cards",HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/clients/current/cards")
    public Set<CardDTO> showCards(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).getCards().stream().map( card -> new CardDTO(card)).collect(Collectors.toSet());
    }
}
