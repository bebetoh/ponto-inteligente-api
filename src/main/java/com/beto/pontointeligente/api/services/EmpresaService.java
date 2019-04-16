package com.beto.pontointeligente.api.services;

import java.util.Optional;

import com.beto.pontointeligente.api.entities.Empresa;

public interface EmpresaService {
	
	/**
	 * Retorna uma empresa dado um CNPJ.
	 * 
	 * @param cnpj
	 * @return Optional<Empresa>
	 */
	Optional<Empresa> bucarPorCnpj(String cnpj);
	
	/**
	 * Cadastra uma nova empresa no banco de dados.
	 * 
	 * @param empresa
	 * @return Empresa
	 */
	Empresa persistir(Empresa empresa);
	

}
