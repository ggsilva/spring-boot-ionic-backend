package com.ggs.cursomc.services.validation;

import static com.ggs.cursomc.domain.enums.TipoCliente.PESSOA_FISICA;
import static com.ggs.cursomc.domain.enums.TipoCliente.PESSOA_JURIDICA;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.ggs.cursomc.dto.ClienteNewDTO;
import com.ggs.cursomc.repositories.ClienteRepository;
import com.ggs.cursomc.resources.exception.FieldMessage;
import com.ggs.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired ClienteRepository clienteRepository;

	private List<FieldMessage> list;
	private ConstraintValidatorContext context;
	private ClienteNewDTO objDto;

	@Override
	public void initialize(ClienteInsert ann) {}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		this.objDto = objDto;
		this.context = context;
		list = new ArrayList<>();

		doValidation();
		finalizeValidation();
		
		return list.isEmpty();
	}

	private void doValidation() {
		validatePessoa();
		validateBah();
	}

	private void validatePessoa() {
		if(isPessoaFisicaValida())
			list.add(new FieldMessage("cpfOuCnpj", "CPF Inválido"));
		
		if(isPessoaJuridicaInvalida())
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ Inválido"));
	}

	private void validateBah() {
		if(clienteRepository.findByEmail(objDto.getEmail()) != null)
			list.add(new FieldMessage("email", "Email já cadastrado"));
	}

	private boolean isPessoaFisicaValida() {
		return isPessoaFisica() && !BR.isValidCpf(objDto.getCpfOuCnpj());
	}

	private boolean isPessoaFisica() {
		return PESSOA_FISICA.getCod().equals(objDto.getTipo());
	}

	private boolean isPessoaJuridicaInvalida() {
		return isPessoaJuridica() && !BR.isValidCnpj(objDto.getCpfOuCnpj());
	}

	private boolean isPessoaJuridica() {
		return PESSOA_JURIDICA.getCod().equals(objDto.getTipo());
	}

	private void finalizeValidation() {
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
	}

}