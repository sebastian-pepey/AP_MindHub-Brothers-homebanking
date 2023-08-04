package com.mindhub.homebanking.repositories;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// Por medio de esta interfaz, se parametriza la clase ClientRepository cuyo parámetro
// es el tipo de objeto que se va a guardar, así como el parámetro del registro que se va
// a emplear.

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {

}
