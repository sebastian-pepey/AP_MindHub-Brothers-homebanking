package com.mindhub.homebanking.services.implement;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map( account -> new AccountDTO(account)).collect(Collectors.toList());
    }
    @Override
    public AccountDTO getAccountById(Long id) {
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }

    @Override
    public Set<AccountDTO> showAccounts(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).getAccounts().stream().map( account -> new AccountDTO(account)).collect(Collectors.toSet());
    }
    @Override
    public boolean existByAccountNumber(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }
    @Override
    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public void saveInRepository(Account account) {
        accountRepository.save(account);
    }
}
