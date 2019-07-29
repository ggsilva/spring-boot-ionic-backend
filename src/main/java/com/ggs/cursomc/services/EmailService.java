package com.ggs.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido p);
	
	void sendEmail(SimpleMailMessage m);
	
	void sendOrderConfirmationHtmlEmail(Pedido p);
	
	void sendHtmlEmail(MimeMessage m);

	void sendNewPasswordEmail(Cliente cliente, String newPass);

}
