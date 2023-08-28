package com.mindhub.homebanking.repositories;
import com.mindhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// Por medio de esta interfaz, se parametriza la clase AccountRepository cuyo parámetro
// es el tipo de objeto que se va a guardar, así como el parámetro del registro que se va
// a emplear.

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
}
