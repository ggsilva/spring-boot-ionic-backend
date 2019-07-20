package com.ggs.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Pedido;
import com.ggs.cursomc.repositories.PedidoRepository;

@Service
public class PedidoService extends AppService<Pedido> {

	@Autowired PedidoRepository repository;

	@Override
	protected PedidoRepository repository() {
		return repository;
	}

}