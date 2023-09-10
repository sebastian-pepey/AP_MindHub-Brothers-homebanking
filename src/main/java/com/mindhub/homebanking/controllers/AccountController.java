package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import com.mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }

    @RequestMapping("/accounts/{id}")
    public ResponseEntity<Object> getAuthClientAccountById(@PathVariable Long id, Authentication authentication) {
        Account account = accountService.findByIdAndClient(id, clientService.findByEmail(authentication.getName()));
        if (account != null) {
            return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/clients/current/accounts")
    public Set<AccountDTO> showAccounts(Authentication authentication) {
        return accountService.showAccounts(authentication);
    }

    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<String> addAccount(Authentication authentication) {
        Client currentClient = clientService.findByEmail(authentication.getName());
        if(currentClient.getAccounts().size() < 3 ){
            String accountNumberCandidate;
            Utils utils = new Utils();
            do {
                accountNumberCandidate = utils.generateRandomAccountNumber();
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
    public ResponseEntity<String> makeTransactions(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam Double amount,
            @RequestParam String description,
            Authentication authentication){

            if(fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || amount == null || description.isEmpty()) {
                return new ResponseEntity<>("One of the field inputs is empty",HttpStatus.FORBIDDEN);
            }

            if( amount <= 0) {
                return new ResponseEntity<>("The amount of the Transfer is zero or incorrect",HttpStatus.FORBIDDEN);
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

            if(accountService.findByAccountNumber(fromAccountNumber).getAccountBalance()<amount) {
                return new ResponseEntity<>("The Client doesn't have enough funds.",HttpStatus.FORBIDDEN);
            }

            Account accountFrom = accountService.findByAccountNumber(fromAccountNumber);
            Account accountTo = accountService.findByAccountNumber(toAccountNumber);

            accountFrom.setAccountBalance(accountFrom.getAccountBalance()-amount);
            Transaction transactionFrom = new Transaction(-1*amount, LocalDateTime.now(), description + " - Debit from account " + accountFrom.getAccountNumber() + " to " + accountTo.getClient().getFirstName() + " " + accountTo.getClient().getLastName(), TransactionType.DEBIT, accountFrom );
            accountTo.setAccountBalance(accountTo.getAccountBalance()+amount);
            Transaction transactionTo = new Transaction(amount, LocalDateTime.now(), description + " - Transfer into account " +  accountTo.getAccountNumber() + " from " + accountFrom.getClient().getFirstName() + " " + accountFrom.getClient().getLastName(), TransactionType.CREDIT, accountTo );

            transactionService.saveInRepository(transactionFrom);
            transactionService.saveInRepository(transactionTo);

            return new ResponseEntity<>("Transaction Created",HttpStatus.CREATED);
        }
}
