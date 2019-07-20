package com.ggs.cursomc.dto;

import java.io.Serializable;

public class CategoriaDTO implements Serializable {

	private static final long serialVersionUID = -1288520541444123597L;
	
	private Integer id;
	private String nome;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
