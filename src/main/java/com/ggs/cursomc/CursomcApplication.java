package com.ggs.cursomc;

import static com.ggs.cursomc.domain.enums.TipoCliente.PESSOA_FISICA;
import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ggs.cursomc.domain.Categoria;
import com.ggs.cursomc.domain.Cidade;
import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.domain.Endereco;
import com.ggs.cursomc.domain.Estado;
import com.ggs.cursomc.domain.Produto;
import com.ggs.cursomc.domain.enums.TipoCliente;
import com.ggs.cursomc.repositories.CategoriaRepository;
import com.ggs.cursomc.repositories.CidadeRepository;
import com.ggs.cursomc.repositories.ClienteRepository;
import com.ggs.cursomc.repositories.EnderecoRepository;
import com.ggs.cursomc.repositories.EstadoRepository;
import com.ggs.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {
	
	@Autowired private CidadeRepository cidadeRepo;
	@Autowired private EstadoRepository estadoRepo;
	@Autowired private ClienteRepository clienteRepo;
	@Autowired private ProdutoRepository produtoRepo;
	@Autowired private EnderecoRepository enderecoRepo;
	@Autowired private CategoriaRepository categoriaRepo;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		createCategorias();
		createEstados();
		createPessoas();
	}

	private void createCategorias() {
		Categoria c1 = newCategoria(null, "Informática");
		Categoria c2 = newCategoria(null, "Escritório");
		
		Produto p1 = newProduto(null, "Computador", 2000.0);
		Produto p2 = newProduto(null, "Impressora", 800.0);
		Produto p3 = newProduto(null, "Mouse", 80.0);
		
		c1.getProduto().addAll(asList(p1, p2, p3));
		c2.getProduto().addAll(asList(p3));
		
		p1.getCategorias().addAll(asList(c1));
		p2.getCategorias().addAll(asList(c1, c2));
		p3.getCategorias().addAll(asList(c1));
		
		categoriaRepo.save(asList(c1, c2));
		produtoRepo.save(asList(p1, p2, p3));
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
		
		e1.getCidades().add(c1);
		e2.getCidades().add(c2);
		e2.getCidades().add(c3);
		
		estadoRepo.save(asList(e1, e2));
		cidadeRepo.save(asList(c1, c2, c3));
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
		
		Cidade maringa = cidadeRepo.findOne(2);
		Cidade curitiba = cidadeRepo.findOne(3);
		
		Endereco e1 = newEndereco(
				"Rua Nardina Rodrigues Johansen",
				"152", "Apto 1107 Tr 02", "87005002", 
				"Vila Bosque", maringa, cl1);
		
		Endereco e2 = newEndereco(
				"Avenida Vicente Machado",
				"152", "Apto 1107 Tr 02", "81850789", 
				"Batel", curitiba, cl1);
		
		cl1.getEnderecos().addAll(asList(e1, e2));

		clienteRepo.save(asList(cl1));
		enderecoRepo.save(asList(e1, e2));
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

}
