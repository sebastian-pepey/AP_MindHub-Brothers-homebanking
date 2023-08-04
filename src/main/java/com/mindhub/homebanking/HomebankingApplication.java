package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
	public CommandLineRunner init(ClientRepository clientRepository) {
		return args -> {
			Client client = new Client("Conejo","Pepito","conejopepito@gmail.com");
			clientRepository.save(client);

		};
	}
}

