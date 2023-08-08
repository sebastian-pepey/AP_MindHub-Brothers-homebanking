package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	// Para indicarle a Spring que ejecute el método a continuación de la anotación @Bean.
	// CommandLineRunner se ejecutará cuando se ejecute la aplicación, inmediatamente luego de montado.
	// Dentro del método, se instancia la clase Cliente previamente creada, en este caso con parámetros
	// correspondientes al cliente de prueba, y dicho objeto se guarda empleando el método save de la clase
	// clientRepository creada la interfaz de repositorios (JPA).
	@Bean
	public CommandLineRunner init(ClientRepository clientRepository, AccountRepository accountRepository) {
		return args -> {
			Client client1 = new Client("Melba","Morel","melbamorel@gmail.com");

			clientRepository.save(client1);

			// Create Account 1
			Account account1 = new Account();

			// Set attributes to Account 1
			account1.setCreationDate(LocalDateTime.now());
			account1.setAccountNumber("VIN001");
			account1.setAccountBalance(5000);
			account1.setClient(client1);

			accountRepository.save(account1);

			// Create Account 2
			Account account2 = new Account();

			// Set attributes to Account 1
			account2.setCreationDate(LocalDateTime.now().plusDays(1));
			account2.setAccountNumber("VIN002");
			account2.setAccountBalance(7500);
			account2.setClient(client1);

			accountRepository.save(account2);
		};
	}
}

