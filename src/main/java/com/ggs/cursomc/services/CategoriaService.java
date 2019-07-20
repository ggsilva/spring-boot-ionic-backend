package com.ggs.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Categoria;
import com.ggs.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService extends AppService<Categoria> {

	@Autowired CategoriaRepository repository;

	@Override
	protected CategoriaRepository repository() {
		return repository;
	}

}
