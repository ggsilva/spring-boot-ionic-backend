package com.ggs.cursomc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ggs.cursomc.services.DBService;
import com.ggs.cursomc.services.EmailService;
import com.ggs.cursomc.services.MockEmailService;
import com.ggs.cursomc.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevConfig {
	
	@Autowired private DBService dbService;
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean 
	public Boolean instantiateDatabase() {
		if (!"create".equals(strategy))
			return false;
		
		dbService.instantiateDatabase();
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		if(isMock())
			return new MockEmailService();
		return new SmtpEmailService();
	}

	private boolean isMock() {
		return true;
	}

}
