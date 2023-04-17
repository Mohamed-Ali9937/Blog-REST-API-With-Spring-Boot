package com.myblog.exception;

import java.io.IOException;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtAuthenticatoinEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		response.setHeader("error", authException.getMessage());
		
		new ObjectMapper().writeValue(response.getOutputStream(), 
				new ErrorResponse(response.getStatus(), "Access Denied", new Date(), 
						request.getRequestURL().toString()));
	}

}
