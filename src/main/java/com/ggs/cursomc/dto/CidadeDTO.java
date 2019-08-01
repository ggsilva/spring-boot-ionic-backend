package com.ggs.cursomc.dto;

import java.io.Serializable;

public class CidadeDTO implements Serializable {

	private static final long serialVersionUID = 784921641404317118L;
	
	private Integer id;
	private String nome;
	private EstadoDTO estado;

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

	public void setEstado(EstadoDTO estado) {
		this.estado = estado;
	}
	
	public EstadoDTO getEstado() {
		return estado;
	}

}
