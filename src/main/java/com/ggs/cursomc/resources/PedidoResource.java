package com.ggs.cursomc.resources;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggs.cursomc.domain.Pedido;
import com.ggs.cursomc.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

	@Autowired private PedidoService service;

	@RequestMapping(method = GET, value = "/{id}")
	public ResponseEntity<?> find(@PathVariable Integer id) {
		return ok(service.find(id));
	}

	@RequestMapping(method = POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido dto) {
		dto = service.realizaPedido(dto);
		return created(newUri(dto)).build();
	}

	private static URI newUri(Pedido p) {
		return fromCurrentRequest().path("/{id}").buildAndExpand(p.getId()).toUri();
	}

}
