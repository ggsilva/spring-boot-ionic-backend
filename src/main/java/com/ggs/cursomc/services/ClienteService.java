package com.ggs.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.repositories.ClienteRepository;
import com.ggs.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	ClienteRepository repository;

	public Cliente buscar(Integer id) {
		Cliente obj = repository.findOne(id);
		if (obj != null)
			return obj;
		
		throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName());
	}

}
