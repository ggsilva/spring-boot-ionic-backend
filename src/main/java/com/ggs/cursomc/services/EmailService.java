package com.ggs.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.ggs.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido p);
	
	void sendEmail(SimpleMailMessage m);

}
