package com.beto.pontointeligente.api.controller;

import javax.validation.Valid;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beto.pontointeligente.api.dtos.CadastroPJDto;
import com.beto.pontointeligente.api.response.Response;
import com.beto.pontointeligente.api.services.EmpresaService;
import com.beto.pontointeligente.api.services.FuncionarioService;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPjController {

	private static final  Logger log = LoggerFactory.getLogger(CadastroPjController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public CadastroPjController() {
		// TODO Auto-generated constructor stub
	}
	
	public ResponseEntity<Response<CadastroPJDto>> cadastrar (@Valid @RequestBody CadastroPJDto cadastroPJDto , BindingResult result){
		log.info("Cadastro PJ: {}", cadastroPJDto.toString() );
		Response<CadastroPJDto> response =  new Response<CadastroPJDto>();
		
		return null;
		
	}

}
