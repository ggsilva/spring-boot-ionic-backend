package com.ggs.cursomc.services;

import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Categoria;
import com.ggs.cursomc.dto.CategoriaDTO;

@Service
public class CategoriaService extends AppService<Categoria> {

	@Override
	protected void updateData(Categoria actualObj, Categoria newObj) {
		actualObj.setNome(newObj.getNome());
	}

	public Categoria fromDTO(CategoriaDTO c) {
		Categoria categoria = new Categoria();
		categoria.setId(c.getId());
		categoria.setNome(c.getNome());
		return categoria;
	}

}
