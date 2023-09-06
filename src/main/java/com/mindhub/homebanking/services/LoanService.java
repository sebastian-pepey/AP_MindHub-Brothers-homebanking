package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface LoanService {

    boolean existsById(Long id);

    Loan findById(long id);

    Set<LoanDTO> showLoans();
}
