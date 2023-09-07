package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Set;

public interface AccountService {
    List<AccountDTO> getAccounts();
    AccountDTO getAccountById(Long id);
    AccountDTO getAuthClientAccountById(Long id);
    Set<AccountDTO> showAccounts(Authentication authentication);
    boolean existByAccountNumber(String accountNumber);
    Account findByAccountNumber(String accountNumber);
    void saveInRepository(Account account);
}
