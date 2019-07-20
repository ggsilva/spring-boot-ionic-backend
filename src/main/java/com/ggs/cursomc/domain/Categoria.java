package com.ggs.cursomc.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Categoria extends AppEntity {

	private static final long serialVersionUID = 2212197860374734710L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	private String nome;
	
	@ManyToMany(mappedBy = "categorias")
	private List<Produto> produto = new ArrayList<Produto>();

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Produto> getProduto() {
		return produto;
	}

}
