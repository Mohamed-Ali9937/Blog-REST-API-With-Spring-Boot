package com.myblog.service;

import com.myblog.dto.request.LoginRequestModel;
import com.myblog.dto.request.RegisterRequestModel;
import com.myblog.dto.response.RegisterResponseModel;

public interface AuthService {

	String login(LoginRequestModel loginRequestModel);
	
	RegisterResponseModel register(RegisterRequestModel registerRequestModel);
	
}
