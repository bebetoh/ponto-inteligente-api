package com.beto.pontointeligente.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.beto.pontointeligente.api.entities.Funcionario;

@Transactional(readOnly = true)
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

	List<Funcionario> findByCpf(String cpf);
	
	List<Funcionario> findByEmail(String email);
	
	List<Funcionario> findByCpfOrEmail(String cpf, String email);
}
