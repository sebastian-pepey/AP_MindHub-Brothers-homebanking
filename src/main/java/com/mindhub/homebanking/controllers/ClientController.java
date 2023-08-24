package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientAuthority;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.mindhub.homebanking.dtos.ClientDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll().stream().map( client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

    @RequestMapping("/validation")
    public boolean isAdmin(Authentication auth) {
        return clientRepository.findByEmail(auth.getName()).getClientAuthority().name().equals("ADMIN");
    }

    @RequestMapping(value = "/changeAuthority", method = RequestMethod.PATCH)
    public ResponseEntity<Object> changeAuthority(@RequestParam String email) {
        Client client=clientRepository.findByEmail(email);
        client.setClientAuthority(ClientAuthority.ADMIN);
        clientRepository.save(client);
        return new ResponseEntity<>("Authority Changed", HttpStatus.OK);
    }

}
