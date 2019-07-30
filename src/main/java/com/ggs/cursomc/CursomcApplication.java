package com.ggs.cursomc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.ggs.cursomc.repositories.DBRepository;
import com.ggs.cursomc.services.S3Service;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired private ApplicationContext appContext;
	@Autowired private S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		DBRepository.instance().setContext(appContext);
		s3Service.uploadFile("C:\\Users\\Guilherme\\Pictures\\Fotos\\intro.jpg");
	}

}
