package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private double MaxAmount;
    @ElementCollection
    private List<Integer> payments;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private  Set<Client> client = new HashSet<>();

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

    public void addClient(Client client){
        client.addLoan(this);
        this.client.add(client);
    }

    public Set<Client> getClient() {
        return client;
    }
}
