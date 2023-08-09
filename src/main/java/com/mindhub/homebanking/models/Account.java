package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


// Por medio de la anotación @Entity, se indica que el objeto, en este caso "Client"
// representará una entidad en la Base de Datos. Es decir, que cada uno de sus atributos
// se corresponderá con una columna de la BD.

@Entity
public class Account {

    // Se indica que el tipo de dato id será gestionado por JPA, es decir que será definido
    // por la API conforme se indica que se guarde un nuevo registro.
    // Lo restante que se efectúa en la definición de la clase, son los atributos, getters y
    // setters, así como el constructor, que en este caso tiene como parámetros dichos atributos.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private Long id;
    private String accountNumber;
    private LocalDate creationDate;
    private double accountBalance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy ="account",fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();


    public Account() { }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transaction.addAccount(this);
        this.transactions.add(transaction);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", creationDate=" + creationDate +
                ", accountBalance=" + accountBalance +
                ", client=" + client +
                ", transactions=" + transactions +
                '}';
    }
}


