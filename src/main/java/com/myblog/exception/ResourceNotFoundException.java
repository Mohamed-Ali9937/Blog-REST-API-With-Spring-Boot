package com.myblog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	private String sourceName;
	private String fieldName;
	private String fieldValue;

	public ResourceNotFoundException(String sourceName, String fieldName, String fieldValue) {
		super(String.format("%s not found with %s : %s", sourceName, fieldName, fieldValue));
		this.sourceName = sourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getSourceName() {
		return sourceName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

}
