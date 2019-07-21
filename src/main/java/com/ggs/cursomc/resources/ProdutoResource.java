package com.ggs.cursomc.resources;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ggs.cursomc.domain.Produto;
import com.ggs.cursomc.dto.ProdutoDTO;
import com.ggs.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService service;
	
	@RequestMapping(method = GET, value = "/{id}")
	public ResponseEntity<?> find(@PathVariable Integer id) {
		return ok(service.find(id));
	}
	
	@RequestMapping(method = GET, value = "/page")
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(name = "nome") String nome,
			@RequestParam(name = "ids") List<Integer> ids,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "24") Integer size,
			@RequestParam(name = "order", defaultValue = "nome") String order,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		Page<Produto> produtos = service.findPage(nome, ids, page, size, order, direction);
		return ok().body(produtos.map(p -> newDto(p)));
	}

	private static ProdutoDTO newDto(Produto p) {
		ProdutoDTO dto = new ProdutoDTO();
		dto.setId(p.getId());
		dto.setNome(p.getNome());
		dto.setPreco(p.getPreco());
		return dto;
	}

}
