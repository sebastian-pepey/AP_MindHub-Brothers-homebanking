package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

// Se crea la clase Transaction. Cosas a tener en cuenta:
// >>   Agregado de la Anotación @Entity para decirle a JPA que esta será una Entidad en la BD
// >>   Se crean las notaciones @Id para indicar a la BD que gestione la creación de nuevos registros
//      con @GeneratedValue (depende de JPA), el cual depende de @GenericGenerator (depende de Hibernate)
//      el cual le dice con "native" que Hibernate se va a encargar de la asignación de ids.
// >>   Se declaran los atributos de la (en este caso) entidad. En particular, @ManyToOne, es la relación
//      que indica que esta Entidad tiene una relación muchos a uno. @JoinColumn es una anotación de
//      JPA que indica que se debe agregar una columna con el id
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private double amount;
    private LocalDateTime date;
    private String description;
    private TransactionType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId")
    private Account account;

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void addAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", account=" + account +
                '}';
    }
}
