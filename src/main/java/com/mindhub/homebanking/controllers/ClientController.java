package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientAuthority;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.mindhub.homebanking.dtos.ClientDTO;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/changeAuthority", method = RequestMethod.PATCH)
    public ResponseEntity<Object> changeAuthority(@RequestParam String email) {
        clientService.changeAuthority(email);
        return new ResponseEntity<>("Authority changed", HttpStatus.OK);
    }

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getClients();}

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password){

        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("At least one of the entered fields is null", HttpStatus.FORBIDDEN);
        }

        if(clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("The email entered is already in use", HttpStatus.FORBIDDEN);
        }

        clientService.saveInRepository(new Client(firstName,lastName,email,passwordEncoder.encode(password), ClientAuthority.CLIENT));
        return new ResponseEntity<>("Client created",HttpStatus.CREATED);
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable long id){
        return clientService.getClient(id);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getConnectedClient(Authentication authentication) {
        return clientService.getConnectedClient(authentication);
    }
}


