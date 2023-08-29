package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
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
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static class LoanParameters {
        private String loanId;
        private String amount;
        private String payments;
        private String toAccountNumber;

        public String getLoanId() {
            return loanId;
        }

        public void setLoanId(String loanId) {
            this.loanId = loanId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPayments() {
            return payments;
        }

        public void setPayments(String payments) {
            this.payments = payments;
        }

        public String getToAccountNumber() {
            return toAccountNumber;
        }

        public void setToAccountNumber(String toAccountNumber) {
            this.toAccountNumber = toAccountNumber;
        }

        public LoanParameters() {
        }
    }

    @Transactional
    @RequestMapping(value = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> applyForLoan(@RequestBody LoanParameters loanBody,Authentication authentication){

        if(loanBody.loanId.isEmpty() || loanBody.amount.isEmpty() || Double.parseDouble(loanBody.amount) == 0 || loanBody.payments.isEmpty() || Double.parseDouble(loanBody.payments) == 0 || loanBody.toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("One of the field inputs is empty", HttpStatus.FORBIDDEN);
        }

        if(!loanRepository.existsById(Long.parseLong(loanBody.loanId))){
            return new ResponseEntity<>("The selected Loan type doesn't exist",HttpStatus.FORBIDDEN);
        }

        if(loanRepository.findById(Long.parseLong(loanBody.loanId)).getMaxAmount() < Double.parseDouble(loanBody.amount)){
            return new ResponseEntity<>("The value requested is more than this Loan's available ",HttpStatus.FORBIDDEN);
        }

        if(!loanRepository.findById(Long.parseLong(loanBody.loanId)).getPayments().contains(Integer.parseInt(loanBody.payments))){
            return new ResponseEntity<>("The amount of Payments required is not available",HttpStatus.FORBIDDEN);
        }

        if(!accountRepository.existsByAccountNumber(loanBody.toAccountNumber)) {
            return new ResponseEntity<>("Destination Account doesn't exist",HttpStatus.FORBIDDEN);
        }

        if(!accountRepository.findByAccountNumber(loanBody.toAccountNumber).getClient().equals(clientRepository.findByEmail(authentication.getName()))) {
            return new ResponseEntity<>("The destination account doesn't belong to the Authenticated Client.",HttpStatus.FORBIDDEN);
        }

        Account accountTo = accountRepository.findByAccountNumber(loanBody.toAccountNumber);
        ClientLoan newLoan = new ClientLoan(clientRepository.findByEmail(authentication.getName()),loanRepository.findById(Long.parseLong(loanBody.loanId)),Double.parseDouble(loanBody.amount),Integer.parseInt(loanBody.payments));

        accountTo.setAccountBalance(accountTo.getAccountBalance()+Double.parseDouble(loanBody.amount));
        Transaction transactionLoan = new Transaction(Double.parseDouble(loanBody.amount), LocalDateTime.now(), "Loan approved", TransactionType.CREDIT, accountTo );

        transactionRepository.save(transactionLoan);
        clientLoanRepository.save(newLoan);

        return new ResponseEntity<>("Loan Created",HttpStatus.CREATED);
    }

    @RequestMapping(value = "/loans")
    public ResponseEntity<Object> showLoans(){
        Set<LoanApplicationDTO> loans = loanRepository.findAll().stream().map(loan -> new LoanApplicationDTO(loan)).collect(Collectors.toSet());
        return new ResponseEntity<>(loans,HttpStatus.OK);
    }
}