package com.beto.pontointeligente.api.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtilsTest {
	
	private static final String SENHA = "123456";
	private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

	@Test
	public final void testSenhaNula() {
		assertNull(PasswordUtils.gerarBCrypt(null));
	}
	
	@Test
	public final void testGerarHashSenha() {
		String hash = PasswordUtils.gerarBCrypt(SENHA);
				
		assertTrue(bCryptEncoder.matches(SENHA, hash));
	}

}
