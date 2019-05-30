package com.beto.pontointeligente.api.controller;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beto.api.security.enums.PerfilEnum;
import com.beto.pontointeligente.api.dtos.CadastroPfDto;
import com.beto.pontointeligente.api.entities.Empresa;
import com.beto.pontointeligente.api.entities.Funcionario;
import com.beto.pontointeligente.api.response.Response;
import com.beto.pontointeligente.api.services.EmpresaService;
import com.beto.pontointeligente.api.services.FuncionarioService;
import com.beto.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPfController {

	private static final  Logger log = LoggerFactory.getLogger(CadastroPfController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public CadastroPfController() {
		// TODO Auto-generated constructor stub
	}
	
	
	@PostMapping
	public ResponseEntity<Response<CadastroPfDto>> cadastrar (@Valid @RequestBody CadastroPfDto cadastroPfDto , BindingResult result){
		
		log.info("Cadastro PF: {}", cadastroPfDto.toString() );
		
		Response<CadastroPfDto> response =  new Response<CadastroPfDto>();
		validarDadosExistentes(cadastroPfDto, result);
		
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPfDto);
				
		if(result.hasErrors()) {
			log.error("Erro validando dados de cadastro PF{}", result.getAllErrors());
			result.getAllErrors().forEach( error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Empresa> empresa = this.empresaService.bucarPorCnpj(cadastroPfDto.getCnpj()); 
		
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterCadastroPfDto(funcionario));
		return ResponseEntity.ok(response);
		
	}
	
	/**
	 * Verifica se a empresa ou funcionário já existem na base de dados.
	 * 
	 * @param cadastroPfDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPfDto cadastroPfDto, BindingResult result) {
		
		Optional<Empresa> empresa = this.empresaService.bucarPorCnpj(cadastroPfDto.getCnpj());
		
		if(!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não existente."));
		}
		
		this.funcionarioService.buscarPorCpf(cadastroPfDto.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));
		
		this.funcionarioService.buscarPorEmail(cadastroPfDto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente")));
	}
	
	
	private Funcionario converterDtoParaFuncionario(CadastroPfDto cadastroPfDto) {
		Funcionario funcionario = new Funcionario();
		
		funcionario.setNome(cadastroPfDto.getNome());
		funcionario.setCpf(cadastroPfDto.getCpf());
		funcionario.setEmail(cadastroPfDto.getEmail());
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPfDto.getSenha()));
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);

		cadastroPfDto.getQtdHorasAlmoco().ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		
		cadastroPfDto.getQtdHorasTrabalhoDia().ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasTrabalhoDia)));
		
		cadastroPfDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
		
		return funcionario;
	}
	
	private CadastroPfDto converterCadastroPfDto(Funcionario funcionario) {
		CadastroPfDto cadastroPfDto = new CadastroPfDto();
		
		cadastroPfDto.setId(funcionario.getId());
		cadastroPfDto.setNome(funcionario.getNome());
		cadastroPfDto.setEmail(funcionario.getEmail());
		cadastroPfDto.setCpf(funcionario.getCpf());
		cadastroPfDto.setCnpj(funcionario.getEmpresa().getCnpj());
		
		cadastroPfDto.setQtdHorasAlmoco( funcionario.getQtdHorasAlmoco() == null ? Optional.ofNullable(null) : Optional.of(Float.toString(funcionario.getQtdHorasAlmoco())));
		cadastroPfDto.setQtdHorasTrabalhoDia( funcionario.getQtdHorasTrabalhoDia() == null ? Optional.ofNullable(null) : Optional.of(Float.toString(funcionario.getQtdHorasTrabalhoDia())));
		cadastroPfDto.setValorHora( funcionario.getValorHora() == null ? Optional.ofNullable(null) : Optional.of(funcionario.getValorHora().toString()));
		
		return cadastroPfDto;
	}

}
