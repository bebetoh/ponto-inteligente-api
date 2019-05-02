package com.beto.pontointeligente.api.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beto.pontointeligente.api.dtos.FuncionarioDto;
import com.beto.pontointeligente.api.dtos.LancamentoDto;
import com.beto.pontointeligente.api.entities.Funcionario;
import com.beto.pontointeligente.api.entities.Lancamento;
import com.beto.pontointeligente.api.enums.TipoEnum;
import com.beto.pontointeligente.api.response.Response;
import com.beto.pontointeligente.api.services.FuncionarioService;
import com.beto.pontointeligente.api.services.LancamentoService;

@RestController
@RequestMapping("/api/lacamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {
	
	
	private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private FuncionarioService funcionarioService;	
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;

	public LancamentoController() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Busca os lançamentos de um funcionario dado seu ID.
	 * 
	 * @param funcionarioId
	 * @param pag
	 * @param ord
	 * @param dir
	 * @return
	 */
	@GetMapping(value = "/funcionario/{funcionarioId}")
	public ResponseEntity<Response<Page<LancamentoDto>>> listarFuncionarioPorId(
			@PathVariable("funcionarioId") Long funcionarioId,
			@RequestParam(value = "pag", defaultValue = "0") Integer pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		
		log.info("Buscando lançamentos por ID do funcionário: {}, página: {}", funcionarioId, pag);
		
		Response<Page<LancamentoDto>> response = new Response<Page<LancamentoDto>>();
		
		PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest);
		Page<LancamentoDto> lancamentosDto = lancamentos.map( lanc -> this.converterLancamentoDto(lanc));
		
		response.setData(lancamentosDto);
		
		return ResponseEntity.ok(response);
		
	}
	
	/**
	 *Retorna um Lançamento por ID
	 * 
	 * @param id
	 * @return ResponseEntity<Response<LancamentoDto>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id){
		log.info("Buscando lançamento por ID: {}", id);
		
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);
		
		if(!lancamento.isPresent()) {
			log.info("Lançamento  não encontrado para o ID: {}", id);
			response.getErrors().add("Lançamento  não encontrado para o ID: "+ id);
			return ResponseEntity.badRequest().body(response);
		}
		response.setData(this.converterLancamentoDto(lancamento.get()));
		
		return ResponseEntity.ok(response);
		
	}
	
	/**
	 * Adiciona um novo lançamento 
	 * 
	 * @param lancamentoDto
	 * @param result
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Response<LancamentoDto>> adicionar(
			@Valid @RequestBody LancamentoDto lancamentoDto,
			BindingResult result){
		log.info("Adicionando um lançamento: {}", lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		validarFuncionario(lancamentoDto, result);
		
		Lancamento lancamento = converterDtoParaLancamento(lancamentoDto, result);
		if(result.hasErrors()) {
			log.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(err -> response.getErrors().add(err.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = this.lancamentoService.persistir(lancamento);
		response.setData(this.converterLancamentoDto(lancamento));
		return ResponseEntity.ok(response);  
		
	}
	
	/**
	 * Atualiza os dado de um lançamento
	 * 
	 * @param id
	 * @param lancamentoDto
	 * @param result
	 * @return
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDto>> atualizar(
			@PathVariable("id") Long id, 
			@Valid @RequestBody LancamentoDto lancamentoDto,
			BindingResult result){
		log.info("Atualizando lançamento: {}", lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		validarFuncionario(lancamentoDto, result);
		
		lancamentoDto.setId(Optional.of(id));
		
		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(err -> response.getErrors().add(err.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = this.lancamentoService.persistir(lancamento);
		response.setData(this.converterLancamentoDto(lancamento));
		return ResponseEntity.ok(response);  
		
	}

	/**
	 * Converte a entidade de lançamento para seu DTO
	 * 
	 * @param lancamento
	 * @return LancamentoDto
	 */
	private LancamentoDto converterLancamentoDto(Lancamento lancamento) {
		LancamentoDto lancamentoDto = new LancamentoDto();
		
		lancamentoDto.setId(Optional.of(lancamento.getId()));
		
		lancamentoDto.setData(this.dateFormat.format(lancamento.getData()));
		lancamentoDto.setDescricao(lancamento.getDescricao());
		lancamentoDto.setFuncionarioId(lancamento.getFuncionario().getId());
		
		lancamentoDto.setTipo(lancamento.getTipo().name());
		
		//BeanUtils.copyProperties(lancamento, lancamentoDto);
		return lancamentoDto;
	}
	
	/**
	 * Valida um funcionário verificando se ele é existente e válido no sistema.
	 * 
	 * @param lancamentoDto
	 * @param result
	 */
	private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result) {
		if(lancamentoDto.getId() == null) {
			result.addError(new ObjectError("funcionario", "Funcionário não informado"));
			return;
		}
		log.info("Validando funcionario id: {}", lancamentoDto.getFuncionarioId());
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());
		
		if(!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionário não encontrado. ID inexistente."));
		}
	}
	
	private Lancamento converterDtoParaLancamento(LancamentoDto lancamentoDto, BindingResult result) {		
		
		Lancamento lancamento = new Lancamento();
		
		if(lancamentoDto.getId().isPresent()) {
			Optional<Lancamento> lancOpt = this.lancamentoService.buscarPorId(lancamentoDto.getId().get());
			if (lancOpt.isPresent()){
				lancamento = lancOpt.get();
			}else {
				result.addError(new ObjectError("lancamento", "Lançamento não encontrado."));
			}
		}else {
			lancamento.setFuncionario(new Funcionario());
			lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
		}
		
		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
		
		if(EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
			lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
		}else {
			result.addError(new ObjectError("tipo","Tipo Inválido"));
		}
		return lancamento;
	}
}
