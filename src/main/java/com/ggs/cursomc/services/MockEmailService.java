package com.ggs.cursomc.services;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService {

	private static final Logger LOG = getLogger(MockEmailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage m) {
		LOG.info("Simulando envio de email...");
		LOG.info(format("\n%s", m.toString()));
		LOG.info("Email enviado");
	}

}
