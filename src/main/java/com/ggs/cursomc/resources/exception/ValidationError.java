package com.ggs.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
	
	private static final long serialVersionUID = -5166780175735195933L;
	
	private List<FieldMessage> fields = new ArrayList<FieldMessage>();

	public List<FieldMessage> getFields() {
		return fields;
	}

	public void addFields(String fieldName, String message) {
		this.fields.add(new FieldMessage(fieldName, message));
	}

}
