package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


// Por medio de la anotación @Entity, se indica que el objeto, en este caso "Client"
// representará una entidad en la Base de Datos. Es decir, que cada uno de sus atributos
// se corresponderá con una columna de la DB.

@Entity
public class Client {

    // Se indica que el tipo de dato id será gestionado por JPA, es decir que será definido
    // por la API conforme se indica que se guarde un nuevo registro.
    // Lo restante que se efectúa en la definición de la clase, son los atributos, getters y
    // setters, así como el constructor, que en este caso tiene como parámetros dichos atributos.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    // Se agrega el Password del cliente
    private String password;
    @OneToMany(mappedBy ="client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "cardholder", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client() { }

    public Client(String name, String surname, String email, String password) {
        this.firstName = name;
        this.lastName = surname;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if(!firstName.isBlank()) {
            this.firstName = firstName;
        }
    }

    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        if(!lastName.isBlank()){
            this.lastName = lastName;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccounts(){
        return accounts;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        this.accounts.add(account);
    }

    public void addLoan(ClientLoan loan){
        loan.setClient(this);
        this.clientLoans.add(loan);
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        card.setCardholder(this);
        this.cards.add(card);
    }

    public Set<ClientLoan> getLoans() {
        return clientLoans;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}


