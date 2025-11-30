package gestionUsaurios.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // Importar
import org.springframework.context.annotation.ComponentScan; // Importar
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // Importar

@SpringBootApplication
@ComponentScan(basePackages = {"config", "controller", "service", "repository", "modelo"})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "modelo")
public class UsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuariosApplication.class, args);
	}

}