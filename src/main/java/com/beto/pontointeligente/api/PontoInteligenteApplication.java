package com.beto.pontointeligente.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.beto.pontointeligente.api.entities.Empresa;
import com.beto.pontointeligente.api.repositories.EmpresaReporitory;

@SpringBootApplication
public class PontoInteligenteApplication {
	
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;

	@Autowired
	private EmpresaReporitory empresaReporitory;
	public static void main(String[] args) {
		SpringApplication.run(PontoInteligenteApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			System.out.println("### Quantidade de elementos por pagina = " + this.qtdPorPagina	);
			String CNPJ = "1212312312312";
			Empresa empresa = new Empresa();
			empresa.setRazaoSocial("Empresa de exemplo");
			empresa.setCnpj(CNPJ);
			this.empresaReporitory.save(empresa);
		};
	}

}
