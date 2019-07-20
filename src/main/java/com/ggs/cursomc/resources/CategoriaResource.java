package com.ggs.cursomc.resources;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggs.cursomc.domain.Categoria;
import com.ggs.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;

	@RequestMapping(method = GET, value = "/{id}")
	public ResponseEntity<?> find(@PathVariable Integer id) {
		return ok(service.find(id));
	}

	@RequestMapping(method = POST)
	public ResponseEntity<Void> insert(@RequestBody Categoria c) {
		c = service.insert(c);
		return created(newUri(c)).build();
	}

	private static URI newUri(Categoria c) {
		return fromCurrentRequest().path("/{id}").buildAndExpand(c.getId()).toUri();
	}

	@RequestMapping(method = PUT, value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody Categoria c, @PathVariable Integer id) {
		c.setId(id);
		c = service.update(c);
		return noContent().build();
	}
	
	@RequestMapping(method = DELETE, value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return noContent().build();
	}

}
