package com.beto.pontointeligente.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.beto.pontointeligente.api.entities.Empresa;
import com.beto.pontointeligente.api.repositories.EmpresaRepository;

@SpringBootApplication
public class PontoInteligenteApplication {
	
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;

	//@Autowired
	//private EmpresaReporitory empresaReporitory;
	public static void main(String[] args) {
		SpringApplication.run(PontoInteligenteApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			System.out.println("### Quantidade de elementos por pagina = " + this.qtdPorPagina	);
			/*
			String CNPJ = "1212312312312";
			Empresa empresa1 = new Empresa();
			empresa1.setRazaoSocial("Empresa de exemplo 1");
			empresa1.setCnpj(CNPJ);
			this.empresaReporitory.save(empresa1);
			
			Empresa empresa2 = new Empresa();
			empresa2.setRazaoSocial("Empresa de exemplo 2");
			empresa2.setCnpj("876876876876");
			this.empresaReporitory.save(empresa2);
			*/
		};
	}

}
