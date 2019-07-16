package com.ggs.cursomc;

import static java.util.Arrays.asList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ggs.cursomc.domain.Categoria;
import com.ggs.cursomc.domain.Cidade;
import com.ggs.cursomc.domain.Estado;
import com.ggs.cursomc.domain.Produto;
import com.ggs.cursomc.repositories.CategoriaRepository;
import com.ggs.cursomc.repositories.CidadeRepository;
import com.ggs.cursomc.repositories.EstadoRepository;
import com.ggs.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {
	
	@Autowired CidadeRepository cidadeRepo;
	@Autowired EstadoRepository estadoRepo;
	@Autowired ProdutoRepository produtoRepo;
	@Autowired CategoriaRepository categoriaRepo;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		createCategorias();
		createEstados();
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

}
