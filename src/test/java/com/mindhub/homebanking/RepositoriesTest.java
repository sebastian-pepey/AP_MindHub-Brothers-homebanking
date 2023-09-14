package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void existsByAccountNumber(){
        boolean exists = accountRepository.existsByAccountNumber("VIN88082306");
        assertThat(exists, is(true));
    }
    @Test
    public void findAccountByIdAndClient(){
        Account account = accountRepository.findByIdAndClient(Long.valueOf(4), clientRepository.findById(1));
        assertThat(account, not(null));
    }

    @Test
    public void findbyCardType(){
        Card card= cardRepository.findByType(CardType.CREDIT);
        assertThat(card, null);
    }

    @Test
    public void existsCard(){
        boolean exists= cardRepository.existsById(107L);
        assertThat(exists, is(true));
    }

    @Test
    public void existsClient(){
        boolean exists= clientRepository.existsById(100L);
        assertThat(exists, is(true));
    }

    @Test
    public void existsNullClient(){
        Client client= clientRepository.findById(1L);
        assertThat(client, is(null));
    }

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void findAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }

    @Test
    public void existTransactions() {
        boolean exist = transactionRepository.existsById(1L);
        assertThat(exist,is(null));
    }

}
