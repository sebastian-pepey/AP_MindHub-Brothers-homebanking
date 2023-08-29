package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;

// Por medio de esta interfaz, se parametriza la clase ClientLoanRepository cuyo parámetro
// es el tipo de objeto que se va a guardar, así como el parámetro del registro que se va
// a emplear.

public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {

    ClientLoan findByClient(long client_id);

}
