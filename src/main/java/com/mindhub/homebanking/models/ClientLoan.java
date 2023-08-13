package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

// Por medio de la anotación @Entity, se indica que el objeto, en este caso "ClientLoan"
// representará una entidad en la Base de Datos. Es decir, que cada uno de sus atributos
// se corresponderá con una columna de la DB.

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private double amount;
    private int payments;

    // @ManyToOne indica el lado N de la relación 1:N. La anotación @JoinColumn nombrada "client_id"
    // indica que el objeto (que representa el lado 1 de la relación, por eso de este lado tenemos un
    // solo objeto, y del otro lado, un Set de objetos) se identificará con el nombre "client_id"
    // dado que en el otro lado de la relación indicamos con "mappedBy" qué Entidad vamos a estar vinculando.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    // Creamos el constructor vacío para que JPARepository lo cree en la DB.
    public ClientLoan() {
    }

    // Por medio de este constructor, se instancia el Objeto (fila), empleando las dos Entidades
    // (Tablas) de la relación N:N, así como los atributos particulares de este préstamo. La idea
    // es que el préstamo sea de un tipo especial, pero considerando un monto, que en algún momento
    // se va a validar con el objeto "loan".

    public ClientLoan(Client client, Loan loan, double amount, int payments ){
        this.client = client;
        this.loan = loan;
        this.amount = amount;
        this.payments = payments;
    }

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public Client getClient() {
        return client;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
