package com.ggs.cursomc.domain;

import static com.ggs.cursomc.domain.enums.Perfil.CLIENTE;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ggs.cursomc.domain.enums.Perfil;
import com.ggs.cursomc.domain.enums.TipoCliente;

@Entity
public class Cliente extends AppEntity {

	private static final long serialVersionUID = -8378752058219018388L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	private String nome;
	
	@Column(unique = true)
	private String email;
	
	private String cpfOuCnpj;
	private Integer tipo;
	
	@JsonIgnore
	private String senha;

	@OneToMany(mappedBy = "cliente", cascade = ALL)
	private List<Endereco> enderecos = newArrayList();

	@ElementCollection
	@CollectionTable(name = "TELEFONE")
	private Set<String> telefones = newHashSet();

	@JsonIgnore
	@OneToMany(mappedBy = "cliente")
	private List<Pedido> pedidos = newArrayList();

	@ElementCollection(fetch = EAGER)
	@CollectionTable(name = "PERFIS")
	private Set<Integer> perfis = newHashSet(CLIENTE.getCod());
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}

	public TipoCliente getTipo() {
		return TipoCliente.toEnum(tipo);
	}

	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo.getCod();
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public List<Endereco> getEnderecos() {
		return enderecos;
	}
	
	public Set<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<String> telefones) {
		this.telefones = telefones;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public Set<Perfil> getPerfis() {
		return perfis.stream().map(i -> Perfil.toEnum(i)).collect(toSet());
	}

	public void addPerfil(Perfil perfis) {
		this.perfis.add(perfis.getCod());
	}

}
