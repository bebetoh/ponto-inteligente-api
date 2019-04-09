package com.beto.pontointeligente.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.beto.pontointeligente.api.entities.Empresa;

public interface EmpresaReporitory extends JpaRepository<Empresa, Long> {

	@Transactional(readOnly = true)
	List<Empresa> findByCnpj(String cnpj);
}
