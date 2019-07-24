package com.ggs.cursomc.services;

import static com.ggs.cursomc.domain.enums.EstadoPagamento.PENDENTE;
import static java.util.Calendar.DAY_OF_MONTH;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.domain.ItemPedido;
import com.ggs.cursomc.domain.PagamentoComBoleto;
import com.ggs.cursomc.domain.PagamentoComCartao;
import com.ggs.cursomc.domain.Pedido;
import com.ggs.cursomc.domain.Produto;
import com.ggs.cursomc.repositories.DBRepository;

@Service
public class PedidoService extends AppService<Pedido> {
	
	@Autowired private EmailService emailService;
	
	@Transactional
	public Pedido realizaPedido(Pedido p) {
		p.setId(null);
		p.setInstante(new Date());
		
		ajustaClientes(p);
		ajustaPagamento(p);
		ajustaItens(p);
		
		DBRepository.save(p);
		DBRepository.save(p.getItens());
		DBRepository.save(p.getPagamento());
		
		emailService.sendOrderConfirmationEmail(p);

		return p;
	}

	private void ajustaClientes(Pedido p) {
		Cliente cliente = DBRepository.findOne(Cliente.class, p.getCliente().getId());
		p.setCliente(cliente);
	}

	private void ajustaPagamento(Pedido p) {
		p.getPagamento().setEstado(PENDENTE);
		p.getPagamento().setPedido(p);
		ajustaDataVencimentoSeNecessario(p);
	}

	private void ajustaDataVencimentoSeNecessario(Pedido p) {
		if (p.getPagamento() instanceof PagamentoComCartao)
			return;
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(DAY_OF_MONTH, 7);
		Date date = calendar.getTime();
		((PagamentoComBoleto)p.getPagamento()).setDataVencimento(date);
	}
	
 	private void ajustaItens(Pedido p) {
 		p.getItens().stream().forEach(i -> ajustaItem(p, i));
	}

	private static void ajustaItem(Pedido p, ItemPedido i) {
		i.setDesconto(0.0);
		i.setPedido(p);
		
		Produto produto = produto(i);
		i.setProduto(produto);
		i.setPreco(produto.getPreco());
	}

	private static Produto produto(ItemPedido item) {
		return DBRepository.findOne(Produto.class, item.getProduto().getId());
	}
	
}