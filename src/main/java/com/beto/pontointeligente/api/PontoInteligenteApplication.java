package com.beto.pontointeligente.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PontoInteligenteApplication {
	
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;

	public static void main(String[] args) {
		SpringApplication.run(PontoInteligenteApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			System.out.println("### Quantidade de elementos por pagina = " + this.qtdPorPagina	);
		};
	}

}
