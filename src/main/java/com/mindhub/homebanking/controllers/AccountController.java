package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/accounts")
    private ResponseEntity<Object> getAccounts(){
        return new ResponseEntity<>("El recurso solicitado no existe más | The requested resource no longer exists | Die angeforderte Ressource existiert nicht mehr", HttpStatus.GONE);
    }

    @RequestMapping("/accounts/{id}")
    private ResponseEntity<Object> getAccountById(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.getAccountById(id),HttpStatus.OK);
    }

    @RequestMapping(value = "/clients/current/accounts")
    public ResponseEntity<Object> showAccounts(Authentication authentication) {
        return new ResponseEntity<>(accountService.showAccounts(authentication),HttpStatus.OK);
    }

    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> addAccount(Authentication authentication) {
        Client currentClient = clientService.findByEmail(authentication.getName());
        if(currentClient.getAccounts().size() < 3 ){
            //SERVICIO
            String accountNumberCandidate;
            do {
                accountNumberCandidate = "VIN"+String.format("%8d",Math.round(Math.random()*(99999999)));
            }while(accountService.existByAccountNumber(accountNumberCandidate));

            Account newAccount = new Account(accountNumberCandidate, LocalDate.now(), 0);
            currentClient.addAccount(newAccount);

            accountService.saveInRepository(newAccount);
            clientService.saveInRepository(currentClient);

            return new ResponseEntity<>("Account added", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("The client can't create more than 3 accounts",HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
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

            if(!accountService.findByAccountNumber(fromAccountNumber).getClient().equals(clientService.findByEmail(authentication.getName()))){
                return new ResponseEntity<>("The Authenticated Client doesn't own the Account",HttpStatus.FORBIDDEN);
            }

            if(!accountService.existByAccountNumber(toAccountNumber)) {
                return new ResponseEntity<>("Destination Account doesn't exist",HttpStatus.FORBIDDEN);
            }

            if(accountService.findByAccountNumber(fromAccountNumber).getAccountBalance()<Double.parseDouble(amount)) {
                return new ResponseEntity<>("The Client doesn't have enough funds.",HttpStatus.FORBIDDEN);
            }

            Account accountFrom = accountService.findByAccountNumber(fromAccountNumber);
            Account accountTo = accountService.findByAccountNumber(toAccountNumber);

            accountFrom.setAccountBalance(accountFrom.getAccountBalance()-Double.parseDouble(amount));
            Transaction transactionFrom = new Transaction(Double.parseDouble(amount), LocalDateTime.now(), description + " - Debit from account " + accountFrom.getAccountNumber() + " " + accountFrom.getClient().getFirstName() + " " + accountFrom.getClient().getLastName(), TransactionType.DEBIT, accountFrom );
            accountTo.setAccountBalance(accountTo.getAccountBalance()+Double.parseDouble(amount));
            Transaction transactionTo = new Transaction(Double.parseDouble(amount), LocalDateTime.now(), description + " - Debit from account " + accountFrom.getAccountNumber() + " " + accountFrom.getClient().getFirstName() + " " + accountFrom.getClient().getLastName(), TransactionType.CREDIT, accountTo );

            transactionService.saveInRepository(transactionFrom);
            transactionService.saveInRepository(transactionTo);

            return new ResponseEntity<>("Transaction Created",HttpStatus.CREATED);
        }
}
