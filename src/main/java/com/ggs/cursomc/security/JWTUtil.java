package com.ggs.cursomc.security;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.expiration}")
	private Long expiration;

	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(newDateExpiration())
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}

	private Date newDateExpiration() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(calendar.getTimeInMillis() + expiration);
		return calendar.getTime();
	}

}
