package com.ggs.cursomc.security;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggs.cursomc.dto.CredenciaisDTO;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
	private JWTUtil jwtUtil;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			return authenticationManager.authenticate(authToken(request));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static UsernamePasswordAuthenticationToken authToken(HttpServletRequest request) throws IOException, JsonParseException, JsonMappingException {
		CredenciaisDTO creds = credenciais(request);
		return new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), newArrayList());
	}

	private static CredenciaisDTO credenciais(HttpServletRequest request) throws IOException, JsonParseException, JsonMappingException {
		return new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
											HttpServletResponse response, 
											FilterChain chain, 
											Authentication authResult) throws IOException, ServletException {
		generateToken(response, authResult);
	}

	private void generateToken(HttpServletResponse response, Authentication authResult) {
		String username = ((UserSS) authResult.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        response.addHeader("Authorization", "Bearer " + token);
	}

}
