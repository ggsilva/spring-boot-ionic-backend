package com.ggs.cursomc.resources.exception;

import static java.lang.System.currentTimeMillis;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.ResponseEntity.status;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.ggs.cursomc.services.exceptions.AuthorizationException;
import com.ggs.cursomc.services.exceptions.DataIntegrityException;
import com.ggs.cursomc.services.exceptions.FileException;
import com.ggs.cursomc.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
		return newResponseStandarError(NOT_FOUND, e);
	}

	private static ResponseEntity<StandardError> newResponseStandarError(HttpStatus httpStatus, Exception e) {
		StandardError err = new StandardError();
		err.setStatus(httpStatus.value());
		err.setMsg(e.getMessage());
		err.setTimestamp(currentTimeMillis());
		return status(httpStatus).body(err);
	}
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request){
		return newResponseStandarError(BAD_REQUEST, e);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request){
		ValidationError err = new ValidationError();
		err.setStatus(UNPROCESSABLE_ENTITY.value());
		err.setMsg("Erro de validação");
		err.setTimestamp(currentTimeMillis());
		
		for (FieldError error : e.getBindingResult().getFieldErrors())
			err.addFields(error.getField(), error.getDefaultMessage());
			
		return status(NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request){
		return newResponseStandarError(FORBIDDEN, e);
	}
	
	@ExceptionHandler(FileException.class)
	public ResponseEntity<StandardError> file(FileException e, HttpServletRequest request){
		return newResponseStandarError(BAD_REQUEST, e);
	}
	
	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandardError> amazonService(AmazonServiceException e, HttpServletRequest request){
		return newResponseStandarError(HttpStatus.valueOf(e.getErrorCode()), e);
	}
	
	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<StandardError> amazonClient(AmazonClientException e, HttpServletRequest request){
		return newResponseStandarError(BAD_REQUEST, e);
	}
	
	@ExceptionHandler(AmazonS3Exception.class)
	public ResponseEntity<StandardError> amazonS3Exception(AmazonS3Exception e, HttpServletRequest request){
		return newResponseStandarError(BAD_REQUEST, e);
	}

}
