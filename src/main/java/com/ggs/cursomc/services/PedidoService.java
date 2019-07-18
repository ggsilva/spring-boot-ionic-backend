package com.ggs.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Pedido;
import com.ggs.cursomc.repositories.PedidoRepository;
import com.ggs.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	PedidoRepository repository;

	public Pedido buscar(Integer id) {
		Pedido obj = repository.findOne(id);
		if (obj != null)
			return obj;
		
		throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName());
	}

}
