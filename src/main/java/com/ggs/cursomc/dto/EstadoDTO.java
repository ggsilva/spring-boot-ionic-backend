package com.ggs.cursomc.dto;

import java.io.Serializable;

public class EstadoDTO implements Serializable {

	private static final long serialVersionUID = 4393012211051911120L;
	
	private Integer id;
	private String nome;

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
