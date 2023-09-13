package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.Set;

public interface AccountService {
    List<AccountDTO> getAccounts();
    AccountDTO getAccountDTOById(Long id);
    Account getAccountById(Long id);
    AccountDTO getAuthClientAccountById(Long id);
    Set<AccountDTO> showAccounts(Authentication authentication);
    boolean existByAccountNumber(String accountNumber);
    Account findByAccountNumber(String accountNumber);
    Account findByIdAndClient(Long id, Client client);
    void saveInRepository(Account account);
}
