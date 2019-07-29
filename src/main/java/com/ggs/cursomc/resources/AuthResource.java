package com.ggs.cursomc.resources;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggs.cursomc.dto.EmailDTO;
import com.ggs.cursomc.security.JWTUtil;
import com.ggs.cursomc.security.UserSS;
import com.ggs.cursomc.services.AuthService;
import com.ggs.cursomc.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired private JWTUtil jwtUtil;
	@Autowired private AuthService authService;

	@RequestMapping(method = POST, value = "/refresh_token")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method = POST, value = "/forgot")
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO request) {
		authService.sendNewPassword(request.getEmail());
		return ResponseEntity.noContent().build();
	}

}
