package com.ggs.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Produto;
import com.ggs.cursomc.repositories.ProdutoRepository;

@Service
public class ProdutoService extends AppService<Produto> {

	@Autowired ProdutoRepository repository;

	@Override
	protected ProdutoRepository repository() {
		return repository;
	}

}