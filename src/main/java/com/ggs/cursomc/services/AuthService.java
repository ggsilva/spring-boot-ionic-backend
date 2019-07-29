package com.ggs.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Categoria;
import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.repositories.ClienteRepository;
import com.ggs.cursomc.repositories.DBRepository;
import com.ggs.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService extends AppService<Categoria> {
	
	@Autowired BCryptPasswordEncoder bcp;
	@Autowired EmailService emailService;

	public void sendNewPassword(String email) {
		Cliente cliente = DBRepository.repository(ClienteRepository.class).findByEmail(email);
		if(cliente == null)
			throw new ObjectNotFoundException("Email não encontrado");
		
		String newPass = newPassword();
		cliente.setSenha(bcp.encode(newPass));
		
		DBRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for (int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		Random rand = new Random();
		int opt = rand.nextInt(3);
		if (opt == 0) { // gera um digito
			return (char) (rand.nextInt(10) + 48);
		}
		else if (opt == 1) { // gera letra maiuscula
			return (char) (rand.nextInt(26) + 65);
		}
		else { // gera letra minuscula
			return (char) (rand.nextInt(26) + 97);
		}
	}

}
