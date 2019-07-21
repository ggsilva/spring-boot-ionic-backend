package com.ggs.cursomc.services;

import static com.ggs.cursomc.repositories.DBRepository.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Categoria;
import com.ggs.cursomc.domain.Produto;
import com.ggs.cursomc.repositories.CategoriaRepository;
import com.ggs.cursomc.repositories.ProdutoRepository;

@Service
public class ProdutoService extends AppService<Produto> {

	public Page<Produto> findPage(String nome, List<Integer> ids, Integer page, Integer size, String order, String direction){
		List<Categoria> categorias = categorias(ids);
		PageRequest pageable = pageRequest(page, size, order, direction);
		return repository(ProdutoRepository.class).findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageable);
	}

	private static List<Categoria> categorias(List<Integer> ids) {
		return repository(CategoriaRepository.class).findAll(ids);
	}
	
	private static PageRequest pageRequest(Integer page, Integer size, String order, String direction) {
		return new PageRequest(page, size, Direction.valueOf(direction), order);
	}
	
}