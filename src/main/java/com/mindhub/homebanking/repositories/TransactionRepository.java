package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

// Por medio de esta interfaz, se parametriza la clase TransactionRepository cuyo parámetro
// es el tipo de objeto que se va a guardar, así como el parámetro del registro que se va
// a emplear.

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByDateBetweenAndDescriptionAndType(LocalDateTime initialDate, LocalDateTime finalDate, String description, TransactionType type);

    List<Transaction> findByDateBetweenAndDescription(LocalDateTime initialDate, LocalDateTime finalDate, String description);
}
