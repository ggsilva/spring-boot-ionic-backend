package com.ggs.cursomc.services;

import static org.slf4j.LoggerFactory.getLogger;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailService extends AbstractEmailService {
	
	private static final Logger LOG = getLogger(SmtpEmailService.class);
	
	@Autowired private MailSender mailSender;
	@Autowired private JavaMailSender javaMailSender;
	
	@Override
	public void sendEmail(SimpleMailMessage m) {
		LOG.info("Enviando email...");
		mailSender.send(m);
		LOG.info("Email enviado");
	}

	@Override
	public void sendHtmlEmail(MimeMessage m) {
		LOG.info("Enviando email...");
		javaMailSender.send(m);
		LOG.info("Email enviado");
	}

}
