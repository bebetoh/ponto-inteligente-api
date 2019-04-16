package com.beto.pontointeligente.api.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.beto.pontointeligente.api.entities.Empresa;
import com.beto.pontointeligente.api.entities.Funcionario;
import com.beto.pontointeligente.api.entities.Lancamento;
import com.beto.pontointeligente.api.enums.PerfilEnum;
import com.beto.pontointeligente.api.enums.TipoEnum;
import com.beto.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTests {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	private Long funcionarioId;
	
	@Before
	public void setUp() throws Exception {
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
		
		Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionario(empresa));
		funcionarioId = funcionario.getId();
		
		this.lancamentoRepository.save(obterDadosLancamento(funcionario));
		
		this.lancamentoRepository.save(obterDadosLancamento(funcionario));
	}

	@After
	public void tearDown() throws Exception {
		//this.empresaRepository.deleteAll();
	}

	@Test
	public final void testBuscarLancamentosPorFuncionarioId() {
		 List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId); 
		 
		 assertEquals(2, lancamentos.size());
	}
	
	@Test
	public final void testBuscarLancametosPorFuncionarioIdPaginado() {
		PageRequest page = PageRequest.of(0, 10, Sort.by(Order.asc("funcionarioId")));
		
		Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId, page);
		
		assertEquals(2, lancamentos.getNumberOfElements());
		
	}
	
	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setCnpj("12312312312123");
		empresa.setRazaoSocial("Empresa de Exemplo");
		return empresa;
	}
	
	private Funcionario obterDadosFuncionario(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("Fulano de Tal");
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setCpf("03406322912");
		funcionario.setEmail("email@email.com");
		funcionario.setEmpresa(empresa);
		return funcionario;
	}
	
	private Lancamento obterDadosLancamento(Funcionario funcionario) {
		Lancamento lancamento = new Lancamento();
		lancamento.setFuncionario(funcionario);
		lancamento.setData(new Date());
		lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
		return lancamento;
	}

}
