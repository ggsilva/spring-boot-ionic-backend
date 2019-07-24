package com.ggs.cursomc.services;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstractEmailService {
	
	private static final Logger LOG = getLogger(SmtpEmailService.class);
	
	@Autowired private MailSender mailSender;
	
	@Override
	public void sendEmail(SimpleMailMessage m) {
		LOG.info("Enviando email...");
		mailSender.send(m);
		LOG.info("Email enviado");
	}

}
