package com.ggs.cursomc.services;

import static java.lang.String.format;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.ggs.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${default.sender}")
	private String sender;

	@Override
	public void sendOrderConfirmationEmail(Pedido p) {
		SimpleMailMessage m = prepareSimpleMailMessageFromPedido(p);
		sendEmail(m);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido p) {
		SimpleMailMessage m = new SimpleMailMessage();
		m.setTo(p.getCliente().getEmail());
		m.setFrom(sender);
		m.setSubject(subject(p));
		m.setSentDate(Calendar.getInstance().getTime());
		m.setText(p.toString());
		return m;
	}

	private String subject(Pedido p) {
		return format("Pedido confirmado! Número: %s", p.getId());
	}

}
