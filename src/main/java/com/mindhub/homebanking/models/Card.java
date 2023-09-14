package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

// Por medio de la anotación @Entity, se indica que el objeto, en este caso "Card"
// representará una entidad en la Base de Datos. Es decir, que cada uno de sus atributos
// se corresponderá con una columna de la DB.

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cardholder_id")
    private Client cardholder;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private int cvv;
    private CardType type;
    private CardColor cardColor;
    private boolean active;

    public Card() { }

    public Card(String number, Client cardholder, LocalDate fromDate, LocalDate thruDate, int cvv, CardType type, CardColor cardColor, boolean isActive) {
        this.number = number;
        this.cardholder = cardholder;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.cvv = cvv;
        this.type = type;
        this.cardColor = cardColor;
        this.active = isActive;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client getCardholder() {
        return cardholder;
    }

    public void setCardholder(Client cardholder) {
        this.cardholder = cardholder;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public CardType getCardType() {
        return type;
    }

    public void setCardType(CardType type) {
        this.type = type;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }
}
