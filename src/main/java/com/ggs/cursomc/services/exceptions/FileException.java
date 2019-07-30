package com.ggs.cursomc.services.exceptions;

public class FileException extends RuntimeException {

	private static final long serialVersionUID = -1519105923554207483L;

	public FileException(String msg) {
		super(msg);
	}
	
	public FileException(String msg, Throwable cause) {
		super(msg, cause);
	}

}