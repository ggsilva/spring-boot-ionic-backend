package com.ggs.cursomc.services;

import static com.ggs.cursomc.domain.enums.EstadoPagamento.PENDENTE;
import static java.util.Calendar.DAY_OF_MONTH;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.domain.ItemPedido;
import com.ggs.cursomc.domain.PagamentoComBoleto;
import com.ggs.cursomc.domain.PagamentoComCartao;
import com.ggs.cursomc.domain.Pedido;
import com.ggs.cursomc.domain.Produto;
import com.ggs.cursomc.repositories.DBRepository;
import com.ggs.cursomc.repositories.PedidoRepository;
import com.ggs.cursomc.security.UserSS;
import com.ggs.cursomc.services.exceptions.AuthorizationException;

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
		
		emailService.sendOrderConfirmationHtmlEmail(p);

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
	
	@Override
	public Page<Pedido> findPage(Integer page, Integer size, String order, String direction) {
		UserSS user = UserService.authenticated();
		
		if(user == null)
			throw new AuthorizationException("Acesso negado");
		
		Cliente cliente = DBRepository.findOne(Cliente.class, user.getId());
		PageRequest pageRequest = new PageRequest(page, size, Direction.valueOf(direction), order);
		return DBRepository.repository(PedidoRepository.class).findByCliente(cliente, pageRequest);
	}
	
}