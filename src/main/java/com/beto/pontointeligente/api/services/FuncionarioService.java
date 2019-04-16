package com.beto.pontointeligente.api.services;

import java.util.Optional;

import com.beto.pontointeligente.api.entities.Funcionario;

public interface FuncionarioService {
	/**
	 * Persistem um funiconário na base de dados.
	 * @param funcionario
	 * @return Funcionario
	 */
	Funcionario persistir(Funcionario funcionario);
	
	/**
	 * Busca e retorna um funcionario dado um cpf
	 * @param cpf
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	/**
	 * Retorna um funcionario dado um email.
	 * @param email
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorEmail(String email);

	/**
	 * Retorna um funcionario dado um ID
	 * @param id
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorId(Long id);
	
	
}
