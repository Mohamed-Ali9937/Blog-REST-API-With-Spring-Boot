package com.myblog.exception;


public class BlogApiException extends RuntimeException {
	private String message;


	public BlogApiException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	

}
