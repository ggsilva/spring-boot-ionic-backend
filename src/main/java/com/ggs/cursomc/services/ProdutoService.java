package com.ggs.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Produto;
import com.ggs.cursomc.repositories.ProdutoRepository;

@Service
public class ProdutoService {
	
	@Autowired
	ProdutoRepository repository;
	
	public Produto buscar(Integer id) {
		return repository.findOne(id);
	}

}
