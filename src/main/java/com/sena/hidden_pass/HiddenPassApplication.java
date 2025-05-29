package com.sena.hidden_pass;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HiddenPassApplication {

	public static void main(String[] args) {

		try {
			// Intenta cargar .env solo si está presente (modo desarrollo)
			Dotenv dotenv = Dotenv.configure()
					.ignoreIfMalformed()
					.ignoreIfMissing() // importante para producción
					.load();

			dotenv.entries().forEach(entry -> {
				// Solo asigna si la variable del sistema NO está ya definida
				if (System.getenv(entry.getKey()) == null && System.getProperty(entry.getKey()) == null) {
					System.setProperty(entry.getKey(), entry.getValue());
				}
			});

		} catch (Exception e) {
			System.out.println("⚠️ No se cargó el archivo .env (modo producción o no existe): " + e.getMessage());
		}

		SpringApplication.run(HiddenPassApplication.class, args);
	}
}