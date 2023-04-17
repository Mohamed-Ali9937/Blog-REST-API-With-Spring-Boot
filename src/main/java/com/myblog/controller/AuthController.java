package com.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myblog.dto.request.LoginRequestModel;
import com.myblog.dto.request.RegisterRequestModel;
import com.myblog.dto.response.OperationStatusResponseModel;
import com.myblog.dto.response.RegisterResponseModel;
import com.myblog.service.AuthService;
import com.myblog.utils.OperationName;
import com.myblog.utils.OperationStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

	private AuthService authService;
	
	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<OperationStatusResponseModel> login(@RequestBody LoginRequestModel loginRequestModel) {
		
		OperationStatusResponseModel operationStatus = new OperationStatusResponseModel();
		
		operationStatus.setOperationName(OperationName.LOGIN.name());
		
		String jwtAuthtoken = authService.login(loginRequestModel);
		
		operationStatus.setOperationStatus(OperationStatus.SUCCESS.name());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtAuthtoken));
		
		return new ResponseEntity<>(operationStatus, headers, HttpStatus.OK);
	}
	

	@PostMapping("/register")
	public ResponseEntity<RegisterResponseModel> register
									(@Valid @RequestBody RegisterRequestModel registerRequest){
		
		RegisterResponseModel response = authService.register(registerRequest);
				
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
}
