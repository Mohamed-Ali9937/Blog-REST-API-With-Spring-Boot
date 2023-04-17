package com.myblog.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
				WebRequest request){
		
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
				new Date(), request.getDescription(false));
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BlogApiException.class)
	public ResponseEntity<ErrorResponse> handleBlogApiException(BlogApiException ex,
				WebRequest request){
		
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
				new Date(), request.getDescription(false));
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleBlogApiException(Exception ex,
				WebRequest request){
		
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
				new Date(), request.getDescription(false));
		
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String>handleMethodArgumentNotValidExceptionv(
			MethodArgumentNotValidException ex,
				WebRequest request){
		
		Map<String, String> errorResponse = new HashMap<>();
		
		for(FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			
			if(errorResponse.containsKey(fieldError.getField())) {
				
				String updatedMessage = String.format("%s..., %s",errorResponse.get(fieldError.getField()), 
						fieldError.getDefaultMessage());
				
				errorResponse.put(fieldError.getField(), updatedMessage);
				
			}else {
				
				errorResponse.put(fieldError.getField(),
						String.format("Invalid Input .. %s", fieldError.getDefaultMessage()));
				
			}
			
		}
		
		return errorResponse;
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
				WebRequest request){
		
		ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(),
				new Date(), request.getDescription(false));
		
		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}
	
}
