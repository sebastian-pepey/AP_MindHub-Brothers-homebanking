package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public ResponseEntity<Object> getClients(){
        return new ResponseEntity<>(clientRepository.findAll().stream().map( client -> new ClientDTO(client)).collect(Collectors.toList()),HttpStatus.OK);
    }

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

    @RequestMapping("/clients/{id}")
    public ResponseEntity<Object> getClient(@PathVariable long id){
    //ClientDTO getClient(@PathVariable long id) {
        //return new ClientDTO(clientRepository.findById(id));
        return new ResponseEntity<>("El recurso solicitado no existe más | The requested resource no longer exists | Die angeforderte Ressource existiert nicht mehr",HttpStatus.GONE);
    }

    @RequestMapping("/validation")
    public boolean isAdmin(Authentication auth) {
        return clientRepository.findByEmail(auth.getName()).getClientAuthority().name().equals("ADMIN");
    }

    @RequestMapping(value = "/changeAuthority", method = RequestMethod.PATCH)
    public ResponseEntity<Object> changeAuthority(@RequestParam String email) {
        Client client=clientRepository.findByEmail(email);
        client.setClientAuthority(ClientAuthority.ADMIN);
        clientRepository.save(client);
        return new ResponseEntity<>("Authority Changed", HttpStatus.OK);
    }

    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> addAccount(Authentication authentication) {

        Client currentClient = clientRepository.findByEmail(authentication.getName());
        if(currentClient.getAccounts().size() < 3 ){
            Account newAccount = new Account();
            // Set attributes of new Account
            newAccount.setCreationDate(LocalDate.now());
            CustomUtils customUtils = new CustomUtils();

            do {
                newAccount.setAccountNumber("VIN"+String.format("%8d",Math.round(Math.random()*(99999999))));
            }while(accountRepository.existsByAccountNumber(newAccount.getAccountNumber()));

            newAccount.setAccountBalance(0);
            currentClient.addAccount(newAccount);
            accountRepository.save(newAccount);
            clientRepository.save(currentClient);
            return new ResponseEntity<>("Account added",HttpStatus.OK);

        } else {
            return new ResponseEntity<>("The client can't create more than 3 accounts",HttpStatus.FORBIDDEN);
        }
    }
    @RequestMapping(value = "/clients/current/accounts")
    public ResponseEntity<Object> showAccounts(Authentication authentication) {
        return new ResponseEntity<>(
                clientRepository.findByEmail(authentication.getName()).getAccounts().stream().map( account -> new AccountDTO(account)).collect(Collectors.toSet())
                ,HttpStatus.OK);
    }

    @RequestMapping(value = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> addCards(Authentication authentication,
                                           @RequestParam String cardType,
                                           @RequestParam String cardColor) {

        Client currentClient = clientRepository.findByEmail(authentication.getName());
        CustomUtils customUtils = new CustomUtils();

        if(currentClient.getCards().stream().filter( card -> card.getCardType() == CardType.valueOf(cardType)).collect(Collectors.toSet()).size() < 3) {
            Card newCard = new Card();
            // Set attributes of new Account
            newCard.setCardholder(currentClient);
            newCard.setCardColor(CardColor.valueOf(cardColor));
            StringBuilder newCardNumber = new StringBuilder(new String());
            Random random = new Random();

            do {
                for(int i=0;i<16;i++) {
                    if(i!=0 && i%4==0) newCardNumber.append(" ");
                    newCardNumber.append(random.nextInt(10));
                }
                newCard.setNumber(String.valueOf(newCardNumber));
            } while(cardRepository.findByNumber(newCard.getNumber()) != null);

            newCard.setFromDate(LocalDate.now());
            newCard.setThruDate(LocalDate.now().plusYears(5));
            newCard.setCardType(CardType.valueOf(cardType));
            newCard.setCvv(random.nextInt(1000));
            currentClient.addCard(newCard);
            cardRepository.save(newCard);
            clientRepository.save(currentClient);
            return new ResponseEntity<>("Card Created",HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("The client can't create more than 3 cards",HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/clients/current/cards")
    public ResponseEntity<Object> showCards(Authentication authentication) {
        return new ResponseEntity<>(
                clientRepository.findByEmail(authentication.getName()).getCards().stream().map( card -> new CardDTO(card)).collect(Collectors.toSet())
                ,HttpStatus.OK);
    }
@Transactional
    @RequestMapping(value = "/transactions")
    public ResponseEntity<Object> makeTransactions(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam String amount,
            @RequestParam String description,
            Authentication authentication){

            if(fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || amount.isEmpty() || description.isEmpty()) {
                return new ResponseEntity<>("One of the field inputs is empty",HttpStatus.FORBIDDEN);
            }

            if(fromAccountNumber.equals(toAccountNumber)){
                return new ResponseEntity<>("Origin and destination accounts are the same",HttpStatus.FORBIDDEN);
            }

            if(!accountRepository.findByAccountNumber(fromAccountNumber).getClient().equals(clientRepository.findByEmail(authentication.getName()))){
                return new ResponseEntity<>("The Authenticated Client doesn't own the Account",HttpStatus.FORBIDDEN);
            }

            if(!accountRepository.existsByAccountNumber(toAccountNumber)) {
                return new ResponseEntity<>("Destination Account doesn't exist",HttpStatus.FORBIDDEN);
            }

            if(accountRepository.findByAccountNumber(fromAccountNumber).getAccountBalance()<Double.parseDouble(amount)) {
                return new ResponseEntity<>("The Client doesn't have enough funds.",HttpStatus.FORBIDDEN);
            }

            Account accountFrom = accountRepository.findByAccountNumber(fromAccountNumber);
            Account accountTo = accountRepository.findByAccountNumber(toAccountNumber);

            accountFrom.setAccountBalance(accountFrom.getAccountBalance()-Double.parseDouble(amount));
            Transaction transactionFrom = new Transaction(Double.parseDouble(amount), LocalDateTime.now(), description, TransactionType.DEBIT, accountFrom );
            accountTo.setAccountBalance(accountTo.getAccountBalance()+Double.parseDouble(amount));
            Transaction transactionTo = new Transaction(Double.parseDouble(amount), LocalDateTime.now(), description, TransactionType.CREDIT, accountTo );

            transactionRepository.save(transactionFrom);
            transactionRepository.save(transactionTo);

    return new ResponseEntity<>("Transaction Created",HttpStatus.CREATED);
    }

}


