package com.ggs.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Categoria;
import com.ggs.cursomc.repositories.CategoriaRepository;
import com.ggs.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	CategoriaRepository repository;

	public Categoria buscar(Integer id) {
		Categoria obj = repository.findOne(id);
		if (obj != null)
			return obj;
		
		throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName());
	}

	public Categoria insert(Categoria c) {
		c.setId(null);
		return repository.save(c);
	}

}
