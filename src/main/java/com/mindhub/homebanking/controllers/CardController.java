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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @GetMapping(value = "/clients/current/cards")
    public Set<CardDTO> showCards(Authentication authentication) {
        return cardService.showCards(clientService.findByEmail(authentication.getName()));
    }

    @PostMapping(value = "/clients/current/cards")
    public ResponseEntity<String> addCards(Authentication authentication,
                                           @RequestParam CardType cardType,
                                           @RequestParam String cardColor) {

        Client currentClient = clientService.findByEmail(authentication.getName());

        if( cardService.countClientCards(currentClient,cardType) < 3) {
            String newCardNumber;
            do {
                newCardNumber = Utils.generateRandomCardNumber();
            } while(cardService.findByNumber(newCardNumber) != null);

            Card newCard = new Card(String.valueOf(newCardNumber), currentClient, LocalDate.now(), LocalDate.now().plusYears(5), Utils.generateRandomCvv(), cardType, CardColor.valueOf(cardColor), true);
            currentClient.addCard(newCard);
            cardService.saveInRepository(newCard);
            clientService.saveInRepository(currentClient);
            return new ResponseEntity<>("Card Created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("The client can't create more than 3 cards",HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/client/current/cards/delete")
    public ResponseEntity<String> deleteCard(
            @RequestParam String cardNumber,
            Authentication authentication
    ){
        try {
            if (cardService.findByNumber(cardNumber).getCardholder() != clientService.findByEmail(authentication.getName())) {
                return new ResponseEntity<>("The Authorized Client does not have the selected Card by Number", HttpStatus.FORBIDDEN);
            }

            Card cardToDelete = cardService.findByNumber(cardNumber);
            cardToDelete.setActive(false);

            cardService.saveInRepository(cardToDelete);

            return new ResponseEntity<>("Card Deleted", HttpStatus.OK);
        } catch (NullPointerException e){
            return new ResponseEntity<>("The card entered was not found in the system", HttpStatus.BAD_REQUEST);
        }
    }
}
