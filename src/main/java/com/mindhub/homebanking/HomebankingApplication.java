package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	// Password Encoder
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	// Para indicarle a Spring que ejecute el método a continuación de la anotación @Bean.
	// CommandLineRunner se ejecutará cuando se ejecute la aplicación, inmediatamente luego de montado.
	// Dentro del método, se instancia la clase Cliente previamente creada, en este caso con parámetros
	// correspondientes al cliente de prueba, y dicho objeto se guarda empleando el método save de la clase
	// clientRepository creada la interfaz de repositorios (JPA).
	@Bean
	public CommandLineRunner init(ClientRepository clientRepository,
								  AccountRepository accountRepository,
								  TransactionRepository transactionRepository,
								  LoanRepository loanRepository,
								  ClientLoanRepository clientLoanRepository,
								  CardRepository cardRepository) {
		return args -> {

			Client client1 = new Client("Melba","Morel","melbamorel@gmail.com", passwordEncoder.encode("oreo123"), ClientAuthority.CLIENT);

			clientRepository.save(client1);

			Client client2 = new Client("Capitan","Beto","capitanbeto@gmail.com", passwordEncoder.encode("pass_invisible"), ClientAuthority.CLIENT);

			clientRepository.save(client2);

			Client admin = new Client("admin","admin","admin@admin.com", passwordEncoder.encode("verruckt"), ClientAuthority.ADMIN);

			clientRepository.save(admin);

			// Create Account 1
			Account account1 = new Account();

			// Set attributes to Account 1
			account1.setCreationDate(LocalDate.now());
			account1.setAccountNumber("VIN001");
			account1.setAccountBalance(5000);

			client1.addAccount(account1);

			accountRepository.save(account1);

			// Create Account 2
			Account account2 = new Account();

			// Set attributes to Account 1
			account2.setCreationDate(LocalDate.now().plusDays(1));
			account2.setAccountNumber("VIN002");
			account2.setAccountBalance(7500);
			client1.addAccount(account2);

			accountRepository.save(account2);

			// Create Transaction 1
			Transaction transaction1 = new Transaction();

			transaction1.setAmount(-500);
			transaction1.setDate(LocalDateTime.now());
			transaction1.setDescription("Compra alfajor");
			transaction1.setType(TransactionType.DEBIT);
			transaction1.addAccount(account1);

			transactionRepository.save(transaction1);

			// Create Transaction 2

			Transaction transaction2 = new Transaction();

			transaction2.setAmount(1000);
			transaction2.setDate(LocalDateTime.now());
			transaction2.setDescription("Venta alfajor");
			transaction2.setType(TransactionType.CREDIT);
			transaction2.addAccount(account1);

			transactionRepository.save(transaction2);

			Loan loan1 = new Loan();

			loan1.setName("Hipotecario");
			loan1.setMaxAmount(500000);
			loan1.setPayments(List.of(12,24,36,48,60));

			loanRepository.save(loan1);

			// Se crean primero los préstamos y los clientes, luego se emplea el constructor para
			// crear la instancia de la entidad intermedia (tabla pivote en la DB).

			ClientLoan clientLoan1 = new ClientLoan(client1, loan1,400000,60);

			clientLoan1.setClient(client1);
			clientLoan1.setLoan(loan1);

			clientLoanRepository.save(clientLoan1);

			Loan loan2 = new Loan();

			loan2.setName("Personal");
			loan2.setMaxAmount(100000);
			loan2.setPayments(List.of(6,12,24));

			loanRepository.save(loan2);

			ClientLoan clientLoan2 = new ClientLoan(client1, loan2,50000,12);

			clientLoanRepository.save(clientLoan2);

			Loan loan3 = new Loan();

			loan3.setName("Automotriz");
			loan3.setMaxAmount(300000);
			loan3.setPayments(List.of(6,12,24,36));

			loanRepository.save(loan3);

			ClientLoan clientLoan3 = new ClientLoan(client2, loan3,50000,36);

			clientLoanRepository.save(clientLoan3);

			Card card1 = new Card("3325 6745 7876 4445", client1, LocalDate.of(2023,04,26), LocalDate.of(2021,04,26).plusYears(5), 990, CardType.DEBIT, CardColor.GOLD);

			cardRepository.save(card1);

			Card card2 = new Card("2234 6745 552 7888", client1, LocalDate.of(2022,04,26), LocalDate.of(2021,04,26).plusYears(5), 750, CardType.DEBIT, CardColor.TITANIUM);

			cardRepository.save(card2);

		};
	}
}

