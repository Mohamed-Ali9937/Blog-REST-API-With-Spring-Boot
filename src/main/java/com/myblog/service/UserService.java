package com.myblog.service;

import com.myblog.dto.request.UserRequestModel;
import com.myblog.dto.response.PageResponse;
import com.myblog.dto.response.UserResponseModel;

public interface UserService {
	
	PageResponse<UserResponseModel> getUsers(int pageNo, int pageSize, String orderBy, String orderDir);
	
	UserResponseModel updateUser(String publicUserId, UserRequestModel userRequestModel);
	
	void deleteUser(String publicUserId);

	UserResponseModel getUser(String publicUserId);
}
