package com.ggs.cursomc;

import static com.ggs.cursomc.domain.enums.EstadoPagamento.PENDENTE;
import static com.ggs.cursomc.domain.enums.EstadoPagamento.QUITADO;
import static com.ggs.cursomc.domain.enums.TipoCliente.PESSOA_FISICA;
import static java.util.Arrays.asList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.ggs.cursomc.domain.Categoria;
import com.ggs.cursomc.domain.Cidade;
import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.domain.Endereco;
import com.ggs.cursomc.domain.Estado;
import com.ggs.cursomc.domain.ItemPedido;
import com.ggs.cursomc.domain.Pagamento;
import com.ggs.cursomc.domain.PagamentoComBoleto;
import com.ggs.cursomc.domain.PagamentoComCartao;
import com.ggs.cursomc.domain.Pedido;
import com.ggs.cursomc.domain.Produto;
import com.ggs.cursomc.domain.enums.EstadoPagamento;
import com.ggs.cursomc.domain.enums.TipoCliente;
import com.ggs.cursomc.repositories.DBRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired private ApplicationContext appContext;	
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		init();
		createCategorias();
		createEstados();
		createPessoas();
		createPedidos();
	}

	private void init() {
		DBRepository.instance().setContext(appContext);
	}

	private void createCategorias() {
		Categoria c1 = newCategoria(null, "Informática");
		Categoria c2 = newCategoria(null, "Escritório");
		Categoria c3 = newCategoria(null, "Cama, mesa e banho");
		Categoria c4 = newCategoria(null, "Eletrônicos");
		Categoria c5 = newCategoria(null, "Jardinagem");
		Categoria c6 = newCategoria(null, "Decoração");
		Categoria c7 = newCategoria(null, "Perfumaria");
		
		Produto p1 = newProduto(null, "Computador", 2000.0);
		Produto p2 = newProduto(null, "Impressora", 800.0);
		Produto p3 = newProduto(null, "Mouse", 80.0);
		
		p1.getCategorias().addAll(asList(c1));
		p2.getCategorias().addAll(asList(c1, c2));
		p3.getCategorias().addAll(asList(c1));
		
		DBRepository.save(asList(c1, c2, c3, c4, c5, c6, c7));
		DBRepository.save(asList(p1, p2, p3));
	}

	private static Produto newProduto(Integer id, String nome, Double preco) {
		Produto p = new Produto();
		p.setId(id);
		p.setNome(nome);
		p.setPreco(preco);
		return p;
	}

	private static Categoria newCategoria(Integer id, String nome) {
		Categoria c = new Categoria();
		c.setId(id);
		c.setNome(nome);
		return c;
	}

	private void createEstados() {
		Estado e1 = newEstado(null, "Minas Gerais");
		Estado e2 = newEstado(null, "Paraná");
		
		Cidade c1 = newCidade(null, "Uberlândia", e1);
		Cidade c2 = newCidade(null, "Maringá", e2);
		Cidade c3 = newCidade(null, "Curitiba", e2);
		
		DBRepository.save(asList(e1, e2));
		DBRepository.save(asList(c1, c2, c3));
	}
	
	private static Cidade newCidade(Integer id, String nome, Estado estado) {
		Cidade c = new Cidade();
		c.setId(id);
		c.setNome(nome);
		c.setEstado(estado);
		return c;
	}
	
	private static Estado newEstado(Integer id, String nome) {
		Estado e = new Estado();
		e.setId(id);
		e.setNome(nome);
		return e;
	}

	private void createPessoas() {
		Cliente cl1 = newCliente(
				"Alana de Paula Pereira Gabriel da Silva", 
				"alanappereira@outlook.com", 
				"08789093976",
				PESSOA_FISICA, 
				asList("(44) 9 9927 7225", "(41) 9 9945 7174"));
		
		Cidade maringa = DBRepository.findOne(Cidade.class, 2);
		Cidade curitiba = DBRepository.findOne(Cidade.class, 3);
		
		Endereco e1 = newEndereco(
				"Rua Nardina Rodrigues Johansen",
				"152", "Apto 1107 Tr 02", "87005002", 
				"Vila Bosque", maringa, cl1);
		
		Endereco e2 = newEndereco(
				"Avenida Vicente Machado",
				"152", "Apto 1107 Tr 02", "81850789", 
				"Batel", curitiba, cl1);
		
		DBRepository.save(asList(cl1));
		DBRepository.save(asList(e1, e2));
	}

	private static Endereco newEndereco(String logradouro, String numero, String complemento, String cep, String bairro, Cidade cidade, Cliente cliente) {
		Endereco e = new Endereco();
		e.setLogradouro(logradouro);
		e.setNumero(numero);
		e.setCep(cep);
		e.setBairro(bairro);
		e.setCidade(cidade);
		e.setComplemento(complemento);
		e.setCliente(cliente);
		return e;
	}

	private static Cliente newCliente(String nome, String email, String cpfOuCnpj, TipoCliente tipo, List<String> telefones) {
		Cliente c = new Cliente();
		c.setNome(nome);
		c.setEmail(email);
		c.setCpfOuCnpj(cpfOuCnpj);
		c.setTipo(tipo);
		c.getTelefones().addAll(telefones);
		return c;
	}

	private void createPedidos() {
		Cliente c1 = DBRepository.findOne(Cliente.class, 1);
		Endereco e1 = DBRepository.findOne(Endereco.class, 1);
		Endereco e2 = DBRepository.findOne(Endereco.class, 2);
		
		Pedido p1 = newPedido(c1, e1, "29/10/2012 19:35");
		Pagamento pCC = newPagamentoCC(p1, QUITADO, 6);
		p1.setPagamento(pCC);
		
		Pedido p2 = newPedido(c1, e2, "11/07/2015 20:25");
		Pagamento pCB = newPagamentoCB(p2, PENDENTE, "17/07/2015 20:00", null);
		p2.setPagamento(pCB);
		
		DBRepository.save(asList(p1, p2));
		DBRepository.save(asList(pCC, pCB));
		
		Produto pr1 = DBRepository.findOne(Produto.class, 1);
		Produto pr2 = DBRepository.findOne(Produto.class, 2);
		Produto pr3 = DBRepository.findOne(Produto.class, 3);
		
		ItemPedido i1 = newItemPedido(p1, pr1, 2000.0, 1, 0.0);
		ItemPedido i2 = newItemPedido(p1, pr3, 80.0, 2, 0.0);
		ItemPedido i3 = newItemPedido(p2, pr2, 800.0, 1, 100.0);
		
		DBRepository.save(asList(i1, i2, i3));
	}

	private static Pedido newPedido(Cliente cliente, Endereco endereco, String date) { 
		Pedido p = new Pedido();
		p.setCliente(cliente);
		p.setEnderecoDeEntrega(endereco);
		p.setInstante(newDate(date));
		return p;
	}

	private static Date newDate(String date) {
		if(date == null)
			return null;
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	private static Pagamento newPagamentoCC(Pedido pedido, EstadoPagamento estado, Integer numeroDeParcelas) {
		PagamentoComCartao p = new PagamentoComCartao();
		p.setNumeroDeParcelas(numeroDeParcelas);
		return setPagamento(p, pedido, estado);
	}
	
	private static Pagamento newPagamentoCB(Pedido pedido, EstadoPagamento estado, String dataVencimento, String dataPagamento) {
		PagamentoComBoleto p = new PagamentoComBoleto();
		p.setDataPagamento(newDate(dataPagamento));
		p.setDataPagamento(newDate(dataVencimento));
		return setPagamento(p, pedido, estado);
	}

	private static Pagamento setPagamento(Pagamento p, Pedido pedido, EstadoPagamento estado) {
		p.setPedido(pedido);
		p.setEstado(estado);
		return p;
	}

	private static ItemPedido newItemPedido(Pedido pedido, Produto produto, Double preco, Integer quantidade, Double desconto) {
		ItemPedido i = new ItemPedido();
		i.setPedido(pedido);
		i.setProduto(produto);
		i.setPreco(preco);
		i.setQuantidade(quantidade);
		i.setDesconto(desconto);
		return i;
	}	
	
}
