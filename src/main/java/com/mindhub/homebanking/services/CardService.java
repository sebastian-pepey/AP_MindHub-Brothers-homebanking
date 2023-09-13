package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.util.Set;

public interface CardService {
    Set<CardDTO> showCards(Client client);
    Card findByNumber(String cardNumber);
    int countClientCards(Client client, CardType cardType);
    void saveInRepository(Card card);
}
