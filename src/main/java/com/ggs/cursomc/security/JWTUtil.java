package com.ggs.cursomc.security;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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

	public boolean isTokenValido(String token) {
		Claims claims = getClaims(token);
		return claims != null ? isTokenValido(claims) : false;
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		}catch (Exception e) {
			return null;
		}
	}
	
	private boolean isTokenValido(Claims claims) {
		String user = claims.getSubject();
		Date expirationDate = claims.getExpiration();
		return user != null && expirationDate != null && now().before(expirationDate);
	}

	private static Date now() {
		return Calendar.getInstance().getTime();
	}

	public String getUsername(String token) {
		Claims claims = getClaims(token);
		return claims != null ? claims.getSubject() : null;
	}

}
