package com.mindhub.homebanking.services.implement;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    //Inyección de dependencias: Repositorios
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void changeAuthority(@RequestParam String email) {
        Client client=clientRepository.findByEmail(email);
        client.setClientAuthority(ClientAuthority.ADMIN);
        clientRepository.save(client);
    }

    @Override
    public List<ClientDTO> getClients(){
        return clientRepository.findAll().stream().map( client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClient(@PathVariable long id){
        return new ClientDTO(clientRepository.findById(id));
    }

    @Override
    public ClientDTO getConnectedClient(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @Override
    public Client findByEmail(String email){
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveInRepository(Client client) {
        clientRepository.save(client);
    }
}
