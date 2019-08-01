package com.ggs.cursomc.resources;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggs.cursomc.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {
	
	@Autowired private EstadoService service;
	
	@RequestMapping(method = GET)
	public ResponseEntity<?> findAll() {
		return ok(service.findAllDTO());
	}
	
	@RequestMapping(method = GET, value = "/{estado_id}/cidades")
	public ResponseEntity<?> cidades(@PathVariable(name = "estado_id") Integer id) {
		return ok(service.findAllCidadesDoEstadoDTO(id));
	}
	
}
