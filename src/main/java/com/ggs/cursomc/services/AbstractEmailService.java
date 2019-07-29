package com.ggs.cursomc.services;

import static java.lang.String.format;

import java.util.Calendar;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ggs.cursomc.domain.Cliente;
import com.ggs.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Autowired private TemplateEngine templateEngine;
	@Autowired private JavaMailSender javaMailSender;

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

	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido p) {
		try {
			MimeMessage m = prepareMimeMessageFromPedido(p);
			sendHtmlEmail(m);
		} catch (MessagingException e) {
			sendOrderConfirmationEmail(p);
		}
	}

	protected MimeMessage prepareMimeMessageFromPedido(Pedido p) throws MessagingException {
		MimeMessage m = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(m, true);
		mmh.setTo(p.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject(subject(p));
		mmh.setSentDate(Calendar.getInstance().getTime());
		mmh.setText(htmlFromTemplatePedido(p), true);
		return m;
	}

	protected String htmlFromTemplatePedido(Pedido p) {
		Context context = new Context();
		context.setVariable("pedido", p);
		return templateEngine.process("email/confirmacaoPedido", context);
	}
	
	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage sm = prepareNewPasswordEmail(cliente, newPass);
		sendEmail(sm);
	}

	protected SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(cliente.getEmail());
		sm.setFrom(sender);
		sm.setSubject("Solicitação de nova senha");
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText("Nova senha: " + newPass);
		return sm;
	}

}
