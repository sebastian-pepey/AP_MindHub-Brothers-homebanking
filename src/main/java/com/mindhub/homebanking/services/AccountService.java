package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.AccountDTO;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;


public interface AccountService {
    List<AccountDTO> getAccounts();
    AccountDTO getAccountById(@PathVariable Long id);
}
