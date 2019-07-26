package com.ggs.cursomc.services;

import static com.ggs.cursomc.repositories.DBRepository.repository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.repositories.ClienteRepository;
import com.ggs.cursomc.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Cliente cliente = repository(ClienteRepository.class).findByEmail(username);
		
		if(cliente == null)
			throw new UsernameNotFoundException(username);
		
		UserSS user = new UserSS();
		user.setId(cliente.getId());
		user.setEmail(cliente.getEmail());
		user.setSenha(cliente.getSenha());
		user.setPerfis(cliente.getPerfis());
		
		return user;
	}

}
