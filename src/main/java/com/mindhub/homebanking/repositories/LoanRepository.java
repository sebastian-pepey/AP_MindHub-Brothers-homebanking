package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// Por medio de esta interfaz, se parametriza la clase LoanRepository cuyo parámetro
// es el tipo de objeto que se va a guardar, así como el parámetro del registro que se va
// a emplear.

@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
