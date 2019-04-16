package com.beto.pontointeligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.beto.pontointeligente.api.entities.Lancamento;

public interface LancamentoService {

	/**
	 *Retorna uma lista paginada de lançamentos de um determinado funcionario 
	 * 
	 * @param funcionarioId
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */
	Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);
	
	/**
	 * Retorna um lancamento por ID
	 * 
	 * @param id
	 * @return Optional<Lancamento> 
	 */
	Optional<Lancamento> buscarPorId(Long id);
	
	/**
	 * Persistem um lancamento na base de dados.
	 * 
	 * @param lancamento
	 * @return Lancamento
	 */
	Lancamento persistir(Lancamento lancamento);
	
	/**
	 * Remove um lancamento da base de dados.
	 * 
	 * @param id
	 */
	void remover(Long id);
}
