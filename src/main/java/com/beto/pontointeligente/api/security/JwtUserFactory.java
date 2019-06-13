package com.beto.pontointeligente.api.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.beto.pontointeligente.api.entities.Funcionario;
import com.beto.pontointeligente.api.security.enums.PerfilEnum;

public class JwtUserFactory {

	public JwtUserFactory() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Converte e gera um JwtUser com base nos dados de um funcionario.
	 * 
	 * @param funcionario
	 * @return JwtUser
	 */
	public static JwtUser create(Funcionario funcionario) {
		return new JwtUser(funcionario.getId(), funcionario.getEmail(), funcionario.getSenha(),
				mapToGrantAuthorities(funcionario.getPerfil()));
	}
	
	/**
	 * Converte o perfil do Usuario para o formato utilizado no Spring Security.
	 * 
	 * @param perfilEnum
	 * @return List<GrantedAuthority>
	 */
	private static List<GrantedAuthority> mapToGrantAuthorities(PerfilEnum 	perfilEnum) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(perfilEnum.toString()));
		return authorities;
	}

}
