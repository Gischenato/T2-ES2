package br.pucrs.matricula_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Matricula_ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Matricula_ServiceApplication.class, args);
		System.out.println("=====================================");
	}

}
