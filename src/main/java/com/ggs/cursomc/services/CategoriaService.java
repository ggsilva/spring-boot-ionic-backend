package com.ggs.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Categoria;
import com.ggs.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	CategoriaRepository repository;
	
	public Categoria buscar(Integer id) {
		return repository.findOne(id);
	}

}
