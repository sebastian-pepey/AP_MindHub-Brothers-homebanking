package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api")
public class LoansController {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Transactional
    @RequestMapping(value = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> applyForLoan(
            @RequestParam String loanId,
            @RequestParam String amount,
            @RequestParam String payments,
            @RequestParam String toAccountNumber,
            Authentication authentication){

        if(loanId.isEmpty() || amount.isEmpty() || Double.parseDouble(amount) == 0 || payments.isEmpty() || Double.parseDouble(payments) == 0 || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("One of the field inputs is empty", HttpStatus.FORBIDDEN);
        }

        if(!loanRepository.existsById(Long.parseLong(loanId))){
            return new ResponseEntity<>("The selected Loan type doesn't exist",HttpStatus.FORBIDDEN);
        }

        if(loanRepository.findById(Long.parseLong(loanId)).getMaxAmount() == Double.parseDouble(amount)){
            return new ResponseEntity<>("The value requested is more than this Loan's available ",HttpStatus.FORBIDDEN);
        }

        if(loanRepository.findById(Long.parseLong(loanId)).getPayments().contains(Integer.parseInt(payments))){
            return new ResponseEntity<>("The amount of Payments required is not available",HttpStatus.FORBIDDEN);
        }

        if(!accountRepository.existsByAccountNumber(toAccountNumber)) {
            return new ResponseEntity<>("Destination Account doesn't exist",HttpStatus.FORBIDDEN);
        }

        if(accountRepository.findByAccountNumber(toAccountNumber).getClient().equals(clientRepository.findByEmail(authentication.getName()))) {
            return new ResponseEntity<>("The destination account doesn't belong to the Authenticated Client.",HttpStatus.FORBIDDEN);
        }

        Account accountTo = accountRepository.findByAccountNumber(toAccountNumber);
        ClientLoan newLoan = new ClientLoan(clientRepository.findByEmail(authentication.getName()),loanRepository.findById(Long.parseLong(loanId)),Double.parseDouble(amount),Integer.parseInt(payments));

        accountTo.setAccountBalance(accountTo.getAccountBalance()+Double.parseDouble(amount));
        Transaction transactionLoan = new Transaction(Double.parseDouble(amount), LocalDateTime.now(), "Loan approved", TransactionType.CREDIT, accountTo );

        transactionRepository.save(transactionLoan);
        clientLoanRepository.save(newLoan);

        return new ResponseEntity<>("Loan Created",HttpStatus.CREATED);
    }

    @RequestMapping(value = "/loans")
    public ResponseEntity<Object> showLoans(Authentication authentication){

        LoanApplicationDTO loanApplicationDTO = new LoanApplicationDTO( clientLoanRepository.findByClient(clientRepository.findByEmail(authentication.getName()).getId()));

        return new ResponseEntity<>(loanApplicationDTO,HttpStatus.OK);
    }
}