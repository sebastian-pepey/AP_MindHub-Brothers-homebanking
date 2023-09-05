package com.mindhub.homebanking.services.implement;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    private ClientService clientService;
    @Autowired
    private CardRepository cardRepository;

    @Override
    public Set<CardDTO> showCards(Client client) {
        return client.getCards().stream().map( card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    @Override
    public Card findByNumber(String cardNumber) {
        return cardRepository.findByNumber(cardNumber);
    }

    @Override
    public int countClientCards(Client client, CardType cardType) {
        return client.getCards().stream().filter( card -> card.getCardType() == cardType).collect(Collectors.toSet()).size();
    }

    @Override
    public void saveInRepository(Card card) {
        cardRepository.save(card);
    }

}
