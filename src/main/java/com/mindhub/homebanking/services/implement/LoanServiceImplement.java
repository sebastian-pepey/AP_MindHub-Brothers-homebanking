package com.mindhub.homebanking.services.implement;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplement implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public boolean existsById(Long id) {
        return loanRepository.existsById(id);
    }

    @Override
    public Loan findById(long id) {
        return loanRepository.findById(id);
    }


    @Override
    public Set<LoanDTO> showLoans(){
        return loanRepository.findAll().stream().map( loan -> new LoanDTO(loan)).collect(Collectors.toSet());
    }
}
