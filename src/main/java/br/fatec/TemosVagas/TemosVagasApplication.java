package br.fatec.TemosVagas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TemosVagasApplication {

	public static void main(String[] args) {

		SpringApplication.run(TemosVagasApplication.class, args);
	}

}
