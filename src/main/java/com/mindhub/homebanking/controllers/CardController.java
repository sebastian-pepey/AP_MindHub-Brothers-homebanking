package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @RequestMapping(value = "/clients/current/cards")
    public Set<CardDTO> showCards(Authentication authentication) {
        return cardService.showCards(clientService.findByEmail(authentication.getName()));
    }

    @RequestMapping(value = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<String> addCards(Authentication authentication,
                                           @RequestParam CardType cardType,
                                           @RequestParam String cardColor) {

        Client currentClient = clientService.findByEmail(authentication.getName());

        if( cardService.countClientCards(currentClient,cardType) < 3) {
            String newCardNumber;
            Random random = new Random();
            Utils utils = new Utils();
            do {
                newCardNumber = utils.generateRandomCardNumber();
            } while(cardService.findByNumber(newCardNumber) != null);

            Card newCard = new Card(String.valueOf(newCardNumber), currentClient, LocalDate.now(), LocalDate.now().plusYears(5), random.nextInt(1000), cardType, CardColor.valueOf(cardColor));
            currentClient.addCard(newCard);
            cardService.saveInRepository(newCard);
            clientService.saveInRepository(currentClient);
            return new ResponseEntity<>("Card Created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("The client can't create more than 3 cards",HttpStatus.FORBIDDEN);
        }
    }
}
