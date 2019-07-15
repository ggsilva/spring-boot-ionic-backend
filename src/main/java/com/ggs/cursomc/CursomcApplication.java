package com.ggs.cursomc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ggs.cursomc.domain.Categoria;
import com.ggs.cursomc.repositories.CategoriaRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {
	
	@Autowired
	CategoriaRepository categoriaRepo;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		categoriaRepo.save(newCategoria(null, "Informática"));
		categoriaRepo.save(newCategoria(null, "Escritório"));
	}

	private static Categoria newCategoria(Integer id, String nome) {
		Categoria c1 = new Categoria();
		c1.setId(id);
		c1.setNome(nome);
		return c1;
	}

}
