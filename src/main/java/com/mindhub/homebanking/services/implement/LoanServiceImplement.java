package com.mindhub.homebanking.services.implement;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplement implements LoanService {

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

    @Override
    public ResponseEntity<Object> applyForLoan(@RequestBody LoanApplicationDTO loanBody, Authentication authentication){

        if(loanBody.getLoanId().isEmpty() || loanBody.getAmount().isEmpty() || Double.parseDouble(loanBody.getAmount()) == 0 || loanBody.getPayments().isEmpty() || Double.parseDouble(loanBody.getPayments()) == 0 || loanBody.getToAccountNumber().isEmpty()) {
            return new ResponseEntity<>("One of the field inputs is empty", HttpStatus.FORBIDDEN);
        }

        if(!loanRepository.existsById(Long.parseLong(loanBody.getLoanId()))){
            return new ResponseEntity<>("The selected Loan type doesn't exist",HttpStatus.FORBIDDEN);
        }

        if(loanRepository.findById(Long.parseLong(loanBody.getLoanId())).getMaxAmount() < Double.parseDouble(loanBody.getAmount())){
            return new ResponseEntity<>("The value requested is more than this Loan's available ",HttpStatus.FORBIDDEN);
        }

        if(!loanRepository.findById(Long.parseLong(loanBody.getLoanId())).getPayments().contains(Integer.parseInt(loanBody.getPayments()))){
            return new ResponseEntity<>("The amount of Payments required is not available",HttpStatus.FORBIDDEN);
        }

        if(!accountRepository.existsByAccountNumber(loanBody.getToAccountNumber())) {
            return new ResponseEntity<>("Destination Account doesn't exist",HttpStatus.FORBIDDEN);
        }

        if(!accountRepository.findByAccountNumber(loanBody.getToAccountNumber()).getClient().equals(clientRepository.findByEmail(authentication.getName()))) {
            return new ResponseEntity<>("The destination account doesn't belong to the Authenticated Client.",HttpStatus.FORBIDDEN);
        }

        Account accountTo = accountRepository.findByAccountNumber(loanBody.getToAccountNumber());
        ClientLoan newLoan = new ClientLoan(clientRepository.findByEmail(authentication.getName()),loanRepository.findById(Long.parseLong(loanBody.getLoanId())),Double.parseDouble(loanBody.getAmount()),Integer.parseInt(loanBody.getPayments()));

        accountTo.setAccountBalance(accountTo.getAccountBalance()+Double.parseDouble(loanBody.getAmount()));
        Transaction transactionLoan = new Transaction(Double.parseDouble(loanBody.getAmount()), LocalDateTime.now(), "Loan approved", TransactionType.CREDIT, accountTo );

        transactionRepository.save(transactionLoan);
        clientLoanRepository.save(newLoan);

        return new ResponseEntity<>("Loan Created",HttpStatus.CREATED);
    }
    @Override
    public ResponseEntity<Object> showLoans(){
        Set<LoanDTO> loans = loanRepository.findAll().stream().map( loan -> new LoanDTO(loan)).collect(Collectors.toSet());
        return new ResponseEntity<>(loans,HttpStatus.OK);
    }
}
