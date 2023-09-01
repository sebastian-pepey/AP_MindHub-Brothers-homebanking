package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map( account -> new AccountDTO(account)).collect(Collectors.toList());
    }
    @Override
    public AccountDTO getAccountById(@PathVariable Long id) {
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }
}
