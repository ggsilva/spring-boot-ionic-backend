package com.ggs.cursomc.resources;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.dto.ClienteDTO;
import com.ggs.cursomc.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;

	@RequestMapping(method = GET, value = "/{id}")
	public ResponseEntity<?> find(@PathVariable Integer id) {
		return ok(service.find(id));
	}

	@RequestMapping(method = PUT, value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO c, @PathVariable Integer id) {
		c.setId(id);
		Cliente cat = service.fromDTO(c);
		cat = service.update(cat);
		return noContent().build();
	}
	
	@RequestMapping(method = DELETE, value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return noContent().build();
	}

	@RequestMapping(method = GET)
	public ResponseEntity<List<ClienteDTO>> findAll() {
		List<ClienteDTO> dtos = service.findAll()
				.stream().map(c -> newDto(c))
				.collect(toList());
		return ok().body(dtos);
	}

	private static ClienteDTO newDto(Cliente c) {
		ClienteDTO dto = new ClienteDTO();
		dto.setId(c.getId());
		dto.setNome(c.getNome());
		dto.setEmail(c.getEmail());
		return dto;
	}

	@RequestMapping(method = GET, value = "/page")
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "24") Integer size,
			@RequestParam(name = "order", defaultValue = "nome") String order,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		Page<Cliente> findPage = service.findPage(page, size, order, direction);
		return ok().body(findPage.map(c -> newDto(c)));
	}

}
