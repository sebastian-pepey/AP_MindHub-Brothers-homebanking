package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
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

    @RequestMapping(value = "/loans")
    public ResponseEntity<Object> showLoans(){
        return loanService.showLoans();
    }

    @Transactional
    @RequestMapping(value = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> applyForLoan(@RequestBody LoanApplicationDTO loanBody, Authentication authentication){
        return loanService.applyForLoan(loanBody,authentication);
    }
}