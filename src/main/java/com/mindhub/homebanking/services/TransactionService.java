package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface TransactionService {
    void saveInRepository(Transaction transaction);

}
