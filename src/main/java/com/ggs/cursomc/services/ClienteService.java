package com.ggs.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.repositories.ClienteRepository;

@Service
public class ClienteService extends AppService<Cliente> {

	@Autowired ClienteRepository repository;

	@Override
	protected ClienteRepository repository() {
		return repository;
	}

}