package com.ggs.cursomc.resources;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggs.cursomc.domain.Categoria;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {
	
	@RequestMapping(method = GET)
	public List<Categoria> listar() {
		List<Categoria> lista = new ArrayList<Categoria>();
		lista.add(newCategoria(1, "Informatica"));
		lista.add(newCategoria(2, "Escritorio"));
		return lista;
	}

	private static Categoria newCategoria(Integer id, String nome) {
		Categoria c1 = new Categoria();
		c1.setId(id);
		c1.setNome(nome);
		return c1;
	}

}
