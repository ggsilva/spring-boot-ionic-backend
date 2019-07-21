package com.ggs.cursomc.services.validation;

import static java.lang.Integer.parseInt;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.dto.ClienteDTO;
import com.ggs.cursomc.repositories.ClienteRepository;
import com.ggs.cursomc.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired private HttpServletRequest request;
	@Autowired private ClienteRepository clienteRepository;

	private List<FieldMessage> list;
	private ConstraintValidatorContext context;
	private ClienteDTO objDto;

	@Override
	public void initialize(ClienteUpdate ann) {}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		this.objDto = objDto;
		this.context = context;
		list = new ArrayList<>();

		doValidation();
		finalizeValidation();
		
		return list.isEmpty();
	}

	private void doValidation() {
		validateEmail();
	}

	private void validateEmail() {
		if(isEmailJaUtilizado())
			list.add(new FieldMessage("email", "Email já cadastrado"));
	}

	private boolean isEmailJaUtilizado() {
		Cliente c = clienteDadoEmail();
		return c != null && !c.getId().equals(idClienteAtualizacao());
	}

	@SuppressWarnings("unchecked")
	private Integer idClienteAtualizacao() {
		Map<String, String> map = (Map<String, String>) request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		return parseInt(map.get("id"));
	}

	private Cliente clienteDadoEmail() {
		return clienteRepository.findByEmail(objDto.getEmail());
	}

	private void finalizeValidation() {
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
	}

}