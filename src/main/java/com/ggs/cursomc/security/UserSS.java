package com.ggs.cursomc.security;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ggs.cursomc.domain.enums.Perfil;

public class UserSS implements UserDetails {

	private static final long serialVersionUID = -3374668635414224928L;
	
	private Integer id;
	private String senha;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public void setPerfis(Set<Perfil> perfis) {
		authorities = perfis.stream().map(p -> new SimpleGrantedAuthority(p.getDescricao())).collect(toSet());
	}
	
	public Integer getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public boolean hasRole(Perfil perfil) {
		return getAuthorities().stream().anyMatch(isPerfil(perfil));
	}

	private static Predicate<GrantedAuthority> isPerfil(Perfil perfil) {
		return new Predicate<GrantedAuthority>() {
			@Override
			public boolean test(GrantedAuthority t) {
				return perfil.getDescricao().equals(t.getAuthority());
			}
		};
	}

}
