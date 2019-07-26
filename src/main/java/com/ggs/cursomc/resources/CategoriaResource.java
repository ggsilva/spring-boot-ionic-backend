package com.ggs.cursomc.resources;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ggs.cursomc.domain.Categoria;
import com.ggs.cursomc.dto.CategoriaDTO;
import com.ggs.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired private CategoriaService service;

	@RequestMapping(method = GET, value = "/{id}")
	public ResponseEntity<?> find(@PathVariable Integer id) {
		return ok(service.find(id));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO c) {
		Categoria cat = service.fromDTO(c);
		cat = service.insert(cat);
		return created(newUri(newDto(cat))).build();
	}

	private static URI newUri(CategoriaDTO c) {
		return fromCurrentRequest().path("/{id}").buildAndExpand(c.getId()).toUri();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = PUT, value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO c, @PathVariable Integer id) {
		c.setId(id);
		Categoria cat = service.fromDTO(c);
		cat = service.update(cat);
		return noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = DELETE, value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return noContent().build();
	}

	@RequestMapping(method = GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		List<CategoriaDTO> dtos = service.findAll()
				.stream().map(c -> newDto(c))
				.collect(toList());
		return ok().body(dtos);
	}

	private static CategoriaDTO newDto(Categoria c) {
		CategoriaDTO dto = new CategoriaDTO();
		dto.setId(c.getId());
		dto.setNome(c.getNome());
		return dto;
	}

	@RequestMapping(method = GET, value = "/page")
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "24") Integer size,
			@RequestParam(name = "order", defaultValue = "nome") String order,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		Page<Categoria> findPage = service.findPage(page, size, order, direction);
		return ok().body(findPage.map(c -> newDto(c)));
	}

}
