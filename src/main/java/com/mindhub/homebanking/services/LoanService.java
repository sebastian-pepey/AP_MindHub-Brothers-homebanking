package com.mindhub.homebanking.services;

import com.mindhub.homebanking.controllers.LoansController;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public interface LoanService {
    ResponseEntity<Object> applyForLoan(@RequestBody LoansController.LoanParameters loanBody, Authentication authentication);
    ResponseEntity<Object> showLoans();
}
