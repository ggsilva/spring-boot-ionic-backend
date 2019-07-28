package com.ggs.cursomc.services;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ggs.cursomc.domain.Cidade;
import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.domain.Endereco;
import com.ggs.cursomc.domain.enums.Perfil;
import com.ggs.cursomc.domain.enums.TipoCliente;
import com.ggs.cursomc.dto.ClienteDTO;
import com.ggs.cursomc.dto.ClienteNewDTO;
import com.ggs.cursomc.repositories.DBRepository;
import com.ggs.cursomc.security.UserSS;
import com.ggs.cursomc.services.exceptions.AuthorizationException;

@Service
public class ClienteService extends AppService<Cliente> {
	
	@Autowired BCryptPasswordEncoder bc;

	@Override
	protected void updateData(Cliente oldObj, Cliente newObj) {
		oldObj.setNome(newObj.getNome());
		oldObj.setEmail(newObj.getEmail());
	}

	public Cliente fromDTO(ClienteDTO c) {
		Cliente cliente = new Cliente();
		cliente.setId(c.getId());
		cliente.setNome(c.getNome());
		cliente.setEmail(c.getEmail());
		return cliente;
	}

	public Cliente fromDTO(ClienteNewDTO dto) {
		Cliente obj = new Cliente();
		obj.setCpfOuCnpj(dto.getCpfOuCnpj());
		obj.setEmail(dto.getEmail());
		obj.setNome(dto.getNome());
		obj.setTelefones(new HashSet<String>(dto.getTelefones()));
		obj.setTipo(TipoCliente.toEnum(dto.getTipo()));
		obj.setSenha(bc.encode(dto.getSenha()));
		
		Cidade cidade = new Cidade();
		cidade.setId(dto.getCidadeId());
		
		Endereco end = new Endereco();
		end.setBairro(dto.getBairro());
		end.setCep(dto.getCep());
		end.setComplemento(dto.getComplemento());
		end.setLogradouro(dto.getLogradouro());
		end.setNumero(dto.getNumero());
		
		end.setCidade(cidade);
		
		end.setCliente(obj);
		
		obj.getEnderecos().add(end);
		
		return obj; 
	}

	@Transactional
	@Override
	public Cliente insert(Cliente c) {
		c.setId(null);
		DBRepository.save(c);
		DBRepository.save(c.getEnderecos());
		return c;
	}
	
	@Override
	public Cliente find(Integer id) {
		if(isUserPermitido(id))
			return super.find(id);
		throw new AuthorizationException("Acesso negado");
	}

	private boolean isUserPermitido(Integer id) {
		UserSS userSS = UserService.authenticated();
		return userSS != null
			&& userSS.hasRole(Perfil.ADMIN)
			&& userSS.getId() == id;
	}

}