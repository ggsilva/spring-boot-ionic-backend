package com.ggs.cursomc.resources.exception;

import static java.lang.System.currentTimeMillis;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ggs.cursomc.services.exceptions.DataIntegrityException;
import com.ggs.cursomc.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		err.setStatus(NOT_FOUND.value());
		err.setMsg(e.getMessage());
		err.setTimestamp(currentTimeMillis());
		return status(NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrityException(DataIntegrityException e, HttpServletRequest request){
		StandardError err = new StandardError();
		err.setStatus(NOT_FOUND.value());
		err.setMsg(e.getMessage());
		err.setTimestamp(currentTimeMillis());
		return status(NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){
		ValidationError err = new ValidationError();
		err.setStatus(NOT_FOUND.value());
		err.setMsg("Erro de validação");
		err.setTimestamp(currentTimeMillis());
		
		for (FieldError error : e.getBindingResult().getFieldErrors())
			err.addFields(error.getField(), error.getDefaultMessage());
			
		return status(NOT_FOUND).body(err);
	}

}
