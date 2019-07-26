package com.ggs.cursomc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTUtil jwtUtil;
	private UserDetailsService userDetailsService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String authorization = request.getHeader("Authorization");
		
		if(isAuthorizationOk(authorization))
			tryAuthenticate(authorization);
		
		chain.doFilter(request, response);
	}

	private boolean isAuthorizationOk(String authorization) {
		return authorization != null && authorization.startsWith("Bearer ");
	}

	private void tryAuthenticate(String authorization) {
		UsernamePasswordAuthenticationToken auth = getAuthentication(authorization.replace("Bearer ", ""));
		if(auth != null)
			SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if(!jwtUtil.isTokenValido(token))
			return null;

		UserDetails user = user(token);
		return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	}

	private UserDetails user(String token) {
		return userDetailsService.loadUserByUsername(jwtUtil.getUsername(token));
	}

}
