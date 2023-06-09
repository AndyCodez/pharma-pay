package com.andrew.pharmapay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PharmaPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PharmaPayApplication.class, args);
	}

}
