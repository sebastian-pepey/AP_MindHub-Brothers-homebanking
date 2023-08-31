package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/accounts")
    private ResponseEntity<Object> getAccounts(){
        return new ResponseEntity<>("El recurso solicitado no existe más | The requested resource no longer exists | Die angeforderte Ressource existiert nicht mehr", HttpStatus.GONE);
    }

    @RequestMapping("/accounts/{id}")
    private ResponseEntity<Object> getAccountById(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.getAccountById(id),HttpStatus.OK);
    }
}
