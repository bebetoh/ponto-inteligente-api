package com.beto.pontointeligente.api.services.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.beto.pontointeligente.api.entities.Lancamento;
import com.beto.pontointeligente.api.repositories.LancamentoRepository;
import com.beto.pontointeligente.api.services.LancamentoService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoServiceImplTest {

	@MockBean
	LancamentoRepository lancamentoRepository;
	
	@Autowired
	LancamentoService lancamentoService;
	
	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.lancamentoRepository.findByFuncionarioId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
		.willReturn(new PageImpl<Lancamento>(new ArrayList<Lancamento>()));
		BDDMockito.given(this.lancamentoRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(new Lancamento()));
		BDDMockito.given(this.lancamentoRepository.save(Mockito.any(Lancamento.class))).willReturn(new Lancamento());
	}

	@Test
	public final void testBuscarPorFuncionarioId() {
		Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(1L, PageRequest.of(0, 10));
		assertNotNull(lancamentos);
	}

	@Test
	public final void testBuscarPorId() {
		Optional<Lancamento> lancamento = this.lancamentoRepository.findById(1L);
		assertTrue(lancamento.isPresent());
	}

	@Test
	public final void testPersistir() {
		Lancamento lancamento = this.lancamentoRepository.save(new Lancamento());
		assertNotNull(lancamento);
	}

	
	public final void testRemover() {
		this.lancamentoRepository.deleteById(1L);
	}

}
