package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.mindhub.homebanking.dtos.ClientDTO;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @RequestMapping(value = "/changeAuthority", method = RequestMethod.PATCH)
    public ResponseEntity<Object> changeAuthority(@RequestParam String email) {
        clientService.changeAuthority(email);
        return new ResponseEntity<>("Authority changed", HttpStatus.OK);
    }

    @RequestMapping("/clients")
    public ResponseEntity<Object> getClients(){
        return clientService.getClients();}

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password){

        return clientService.register(firstName,lastName,email,password);
    }

    @RequestMapping("/clients/{id}")
    public ResponseEntity<Object> getClient(@PathVariable long id){
        return new ResponseEntity<>("El recurso solicitado no existe más | The requested resource no longer exists | Die angeforderte Ressource existiert nicht mehr",HttpStatus.GONE);
    }

    @RequestMapping("/clients/current")
    public ResponseEntity<Object> getConnectedClient(Authentication authentication) {
        return new ResponseEntity<>(clientService.getConnectedClient(authentication),HttpStatus.OK);
    }

    @RequestMapping(value = "/clients/current/accounts")
    public ResponseEntity<Object> showAccounts(Authentication authentication) {
        return new ResponseEntity<>(clientService.showAccounts(authentication),HttpStatus.OK);
    }

    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> addAccount(Authentication authentication) {
        return clientService.addAccount(authentication);
    }

    @RequestMapping(value = "/clients/current/cards")
    public ResponseEntity<Object> showCards(Authentication authentication) {
        return clientService.showCards(authentication);
    }

    @RequestMapping(value = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> addCards(Authentication authentication,
                                           @RequestParam String cardType,
                                           @RequestParam String cardColor) {
        return clientService.addCards(authentication,cardType,cardColor);
    }
@Transactional
    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> makeTransactions(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam String amount,
            @RequestParam String description,
            Authentication authentication){
        return clientService.makeTransactions(fromAccountNumber,toAccountNumber,amount,description,authentication);
    }

    @RequestMapping("/validation")
    public boolean isAdmin(Authentication authentication) {
        return clientService.isAdmin(authentication);
    }

}


