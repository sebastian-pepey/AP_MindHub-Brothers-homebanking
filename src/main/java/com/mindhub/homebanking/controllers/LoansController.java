package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping(value = "/api")
public class LoansController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @RequestMapping(value = "/loans")
    public Set<LoanDTO> showLoans(){
        return loanService.showLoans();
    }

    @Transactional
    @PostMapping(value = "/loans")
    public ResponseEntity<String> applyForLoan(@RequestBody LoanApplicationDTO loanBody, Authentication authentication){

        if(loanBody.getLoanId() == null || loanBody.getAmount() == null || loanBody.getPayments() == null  || loanBody.getToAccountNumber() == null) {
            return new ResponseEntity<>("One of the field inputs is empty", HttpStatus.FORBIDDEN);
        }

        if(Double.compare(loanBody.getAmount(),0)  <= 0 || Double.compare(loanBody.getPayments(),0) <= 0) {
            return new ResponseEntity<>("One of the numeric values is incorrect", HttpStatus.FORBIDDEN);
        }

        if(!loanService.existsById(loanBody.getLoanId())){
            return new ResponseEntity<>("The selected Loan type doesn't exist",HttpStatus.FORBIDDEN);
        }

        if(loanService.findById(loanBody.getLoanId()).getMaxAmount() < loanBody.getAmount()){
            return new ResponseEntity<>("The value requested is more than this Loan's available ",HttpStatus.FORBIDDEN);
        }

        if(!loanService.findById(loanBody.getLoanId()).getPayments().contains(loanBody.getPayments())){
            return new ResponseEntity<>("The amount of Payments required is not available",HttpStatus.FORBIDDEN);
        }

        if(!accountService.existByAccountNumber(loanBody.getToAccountNumber())) {
            return new ResponseEntity<>("Destination Account doesn't exist",HttpStatus.FORBIDDEN);
        }

        if(!accountService.findByAccountNumber(loanBody.getToAccountNumber()).getClient().equals(clientService.findByEmail(authentication.getName()))) {
            return new ResponseEntity<>("The destination account doesn't belong to the Authenticated Client.",HttpStatus.FORBIDDEN);
        }

        Account accountTo = accountService.findByAccountNumber(loanBody.getToAccountNumber());
        ClientLoan newLoan = new ClientLoan(clientService.findByEmail(authentication.getName()),loanService.findById(loanBody.getLoanId()),loanBody.getAmount(),loanBody.getPayments());

        accountTo.setAccountBalance(accountTo.getAccountBalance()+loanBody.getAmount());
        Transaction transactionLoan = new Transaction(loanBody.getAmount(), LocalDateTime.now(), "Loan approved", TransactionType.CREDIT, accountTo );

        transactionService.saveInRepository(transactionLoan);
        clientService.saveCLInRepository(newLoan);

        return new ResponseEntity<>("Loan Created",HttpStatus.CREATED);
    }
}