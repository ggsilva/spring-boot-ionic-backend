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
import org.springframework.web.multipart.MultipartFile;

import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.dto.ClienteDTO;
import com.ggs.cursomc.dto.ClienteNewDTO;
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
	
	@RequestMapping(method = GET, value = "/email")
	public ResponseEntity<?> find(@RequestParam(value = "value") String email) {
		return ok(service.findByEmail(email));
	}

	@RequestMapping(method = PUT, value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO c, @PathVariable Integer id) {
		c.setId(id);
		Cliente cat = service.fromDTO(c);
		cat = service.update(cat);
		return noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = DELETE, value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
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

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = GET, value = "/page")
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "24") Integer size,
			@RequestParam(name = "order", defaultValue = "nome") String order,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		Page<Cliente> findPage = service.findPage(page, size, order, direction);
		return ok().body(findPage.map(c -> newDto(c)));
	}

	@RequestMapping(method = POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO dto) {
		Cliente obj = service.fromDTO(dto);
		obj = service.insert(obj);
		return created(newUri(obj)).build();
	}

	private static URI newUri(Cliente c) {
		return fromCurrentRequest().path("/{id}").buildAndExpand(c.getId()).toUri();
	}

	@RequestMapping(method = POST, value = "/picture")
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file") MultipartFile file) {
		URI uri = service.uploadProfilePicture(file);
		return created(uri).build();
	}

}
