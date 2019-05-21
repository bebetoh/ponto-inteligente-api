package com.beto.pontointeligente.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.beto.api.security.enums.PerfilEnum;
import com.beto.pontointeligente.api.entities.Empresa;
import com.beto.pontointeligente.api.entities.Funcionario;
import com.beto.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTests {

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EmpresaRepository empresaReporitory;
	
	private static final String EMAIL = "email@email.com";
	private static final String CPF = "123123123123";
	
	
	@Before
	public void setUp() throws Exception {
		Empresa empresa = this.empresaReporitory.save(obterDadosEmpresa());
		this.funcionarioRepository.save(this.funcionarioRepository.save(obterDadosFuncionario(empresa)));
	}

	@After
	public void tearDown() throws Exception {
		this.empresaReporitory.deleteAll();
	}
	
	@Test
	public final void testBuscarFuncionarioPorEmail() {
		Funcionario funcionario = this.funcionarioRepository.findByEmail(EMAIL);
		
		assertEquals(EMAIL, funcionario.getEmail());
	}

	@Test
	public final void testBuscarFuncionarioPorCpf() {
		Funcionario funcionario = this.funcionarioRepository.findByCpf(CPF);
		
		assertEquals(CPF, funcionario.getCpf());
	}	
	
	@Test
	public final void testBuscarFuncinarioPorEmailOuCpf() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, EMAIL);
		
		assertNotNull(funcionario);
	}

	@Test
	public final void testBuscarFuncinarioPorEmailOuCpfParaEmailInvalido() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, "email@invalido.com");
		
		assertNotNull(funcionario);
	}
	
	@Test
	public final void testBuscarFuncinarioPorEmailOuCpfParaCpfInvalido() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail("423423234", EMAIL);
		
		assertNotNull(funcionario);
	}

	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setCnpj("12312312312123");
		empresa.setRazaoSocial("Empresa de Exepo");
		return empresa;
	}
	
	private Funcionario obterDadosFuncionario(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("Fulano de Tal");
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setCpf(CPF);
		funcionario.setEmail(EMAIL);
		funcionario.setEmpresa(empresa);
		return funcionario;
	}
	
}
