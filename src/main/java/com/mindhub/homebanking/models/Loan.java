package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Por medio de la anotación @Entity, se indica que el objeto, en este caso "Loan"
// representará una entidad en la Base de Datos. Es decir, que cada uno de sus atributos
// se corresponderá con una columna de la DB.

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private double MaxAmount;

    // @ElementCollection es una anotación de JPA que se emplea para mapear
    // una colección de valores incrustados como una entidad separada en la DB
    @ElementCollection
    private List<Integer> payments;

    // @OneToMany indica el lado 1 de la relación 1:N. El parámetro mappedBy es el
    // atributo (Columna) de la Entidad (Tabla) que contiene los N registros en la relación 1:N.
    // Colocar la anotación arriba del atributo indica que el Set (nota: al ser Set va a traer
    // los datos desordenados, vinculará las tablas, colocando el id (PK) de esta Entidad (Tabla)
    // como FK en la entidad restante.
    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clients = new HashSet<>();

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getMaxAmount() {
        return MaxAmount;
    }
    public void setMaxAmount(double maxAmount) {
        MaxAmount = maxAmount;
    }
    public List<Integer> getPayments() {
        return payments;
    }
    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public void addClientLoan(ClientLoan client){
        client.setLoan(this);
        clients.add(client);
    }
    public Set<ClientLoan> getClient() {
        return clients;
    }
}
