package com.ggs.cursomc.resources.exception;

import static java.lang.System.currentTimeMillis;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ggs.cursomc.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> ObjectNotFound(ObjectNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		err.setStatus(NOT_FOUND.value());
		err.setMsg(e.getMessage());
		err.setTimestamp(currentTimeMillis());
		return status(NOT_FOUND).body(err);
	}

}
