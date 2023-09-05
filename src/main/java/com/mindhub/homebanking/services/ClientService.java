package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ClientService {
    void changeAuthority(@RequestParam String email);
    List<ClientDTO> getClients();
    ClientDTO getClient(@PathVariable long id);
    ClientDTO getConnectedClient(Authentication authentication);
    Client findByEmail(String email);
    void saveInRepository(Client client);
}
