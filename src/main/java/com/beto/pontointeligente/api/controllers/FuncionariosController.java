package com.beto.pontointeligente.api.controllers;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beto.pontointeligente.api.dtos.FuncionarioDto;
import com.beto.pontointeligente.api.entities.Funcionario;
import com.beto.pontointeligente.api.response.Response;
import com.beto.pontointeligente.api.services.FuncionarioService;
import com.beto.pontointeligente.api.utils.PasswordUtils;

@RestController 
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionariosController {

	
	private static final Logger log = LoggerFactory.getLogger(FuncionariosController.class);

	
	@Autowired
	private FuncionarioService funcionarioService;
	
	
	public FuncionariosController() {
		// TODO Auto-generated constructor stub
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<FuncionarioDto>> atualizar(
			@PathVariable("id") Long id,
			@Valid @RequestBody FuncionarioDto funcionarioDto, 
			BindingResult result){	
		
		log.info("Atualizando funcionario: {}", funcionarioDto.toString());
		Response<FuncionarioDto> response = new Response<FuncionarioDto>();
		
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(id);
		if(!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionario não encontrado."));
		}
		
		this.atualizarDadosFuncionario(funcionario.get(), funcionarioDto, result);
		
		if(result.hasErrors()) {
			log.error("Error validando funcionário: {} ", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.converterFuncionarioDto(funcionario.get()));
		
		this.funcionarioService.persistir(funcionario.get());
		
		
		return ResponseEntity.ok(response);
		
	}
	
	private void atualizarDadosFuncionario(Funcionario funcionario, FuncionarioDto funcionarioDto, BindingResult result) {
		funcionario.setNome(funcionarioDto.getNome());
		
		if(!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
			this.funcionarioService.buscarPorEmail(funcionarioDto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));
			funcionario.setEmail(funcionarioDto.getEmail());
		}
		
		funcionarioDto.getQtdHorasAlmoco()
			.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		
		funcionarioDto.getQtdHorasTrabalhoDia()
			.ifPresent(qththd -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qththd)));
		
		funcionarioDto.getValorHora()
			.ifPresent(vh -> funcionario.setValorHora(new BigDecimal(vh)));
		
		if(funcionarioDto.getSenha().isPresent()) {
			funcionario.setSenha(PasswordUtils.gerarBCrypt(funcionarioDto.getSenha().get()));
		}
			
		
	}
	
	/**
	 * Retorna um DTO com os dados de um funcionário.
	 * 
	 * @param funcionario
	 * @return FuncionarioDto
	 */
	private FuncionarioDto converterFuncionarioDto(Funcionario funcionario) {
		FuncionarioDto funcionarioDto = new FuncionarioDto();
		funcionarioDto.setId(funcionario.getId());
		funcionarioDto.setNome(funcionario.getNome());
		
		if(!StringUtils.isEmpty(funcionario.getEmail())){
			funcionarioDto.setEmail(funcionario.getEmail());
		}
		
		if(!StringUtils.isEmpty(funcionarioDto.getQtdHorasAlmoco())) {
			funcionarioDto.setQtdHorasAlmoco(Optional.of(funcionario.getQtdHorasAlmoco().toString()));
		}
		if(funcionario.getValorHora() != null) {
			funcionarioDto.setValorHora(Optional.of(funcionario.getValorHora().toString()));
		}
		if(funcionario.getQtdHorasTrabalhoDia() != null) {
			funcionarioDto.setQtdHorasTrabalhoDia(Optional.of(funcionario.getQtdHorasTrabalhoDia().toString()));
		}
		
		return funcionarioDto;
	}
}
