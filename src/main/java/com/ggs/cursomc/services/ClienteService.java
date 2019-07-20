package com.ggs.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.dto.ClienteDTO;
import com.ggs.cursomc.repositories.ClienteRepository;

@Service
public class ClienteService extends AppService<Cliente> {

	@Autowired ClienteRepository repository;

	@Override
	protected ClienteRepository repository() {
		return repository;
	}
	
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

}