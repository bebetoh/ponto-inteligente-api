/**
 * 
 */
package com.beto.pontointeligente.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.beto.pontointeligente.api.entities.Empresa;

/**
 * @author hildeberto
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaReporitoryTests {

	@Autowired
	private EmpresaReporitory empresaReporitory;
	
	private static final String CNPJ = "12312312312312";
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj(CNPJ);
		this.empresaReporitory.save(empresa);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.empresaReporitory.deleteAll();
	}

	@Test
	public final void testBuscarEmpresaPorCnpj() {
		Empresa empresa = this.empresaReporitory.findByCnpj(CNPJ); 
		assertEquals(CNPJ, empresa.getCnpj());
	}

}
