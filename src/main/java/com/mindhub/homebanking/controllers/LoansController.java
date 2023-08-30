package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;


@RestController
@RequestMapping(value = "/api")
public class LoansController {

    @Autowired
    private LoanService loanService;

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
        return loanService.applyForLoan(loanBody,authentication);
    }

    @RequestMapping(value = "/loans")
    public ResponseEntity<Object> showLoans(){
        return loanService.showLoans();
    }
}