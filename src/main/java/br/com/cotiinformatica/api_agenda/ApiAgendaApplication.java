package br.com.cotiinformatica.api_agenda;


import org.springframework.amqp.rabbit.annotation.EnableRabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class ApiAgendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiAgendaApplication.class, args);

	}

}
