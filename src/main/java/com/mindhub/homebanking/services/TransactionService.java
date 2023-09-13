package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    void saveInRepository(Transaction transaction);
    List<TransactionDTO> findByDateBetweenAndAccount(LocalDateTime minSearchDate, LocalDateTime maxSearchDate, Account account);
}
