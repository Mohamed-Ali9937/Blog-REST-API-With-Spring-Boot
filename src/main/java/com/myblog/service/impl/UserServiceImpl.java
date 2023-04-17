package com.myblog.service.impl;


import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myblog.dto.request.UserRequestModel;
import com.myblog.dto.response.PageResponse;
import com.myblog.dto.response.UserResponseModel;
import com.myblog.entity.UserEntity;
import com.myblog.exception.BlogApiException;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.repository.UserRepository;
import com.myblog.service.UserService;
import com.myblog.utils.UserRoles;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
			PasswordEncoder passwordEncoder) {
		
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public PageResponse<UserResponseModel> getUsers(int pageNo, int pageSize, String orderBy, String orderDir) {
		
		Sort sort = orderDir.equalsIgnoreCase("desc") ? Sort.by(orderBy).descending() :
			Sort.by(orderBy).ascending();
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page <UserEntity> userEntityPage = userRepository.findAll(pageable);
		
		List<UserEntity> userEntityList = userEntityPage.getContent();
		
		Type typeList = new TypeToken<List<UserResponseModel>>(){}.getType();
		
		List<UserResponseModel> userResponseModel = modelMapper.map(userEntityList, typeList);
		
		PageResponse<UserResponseModel> pageResponse = new PageResponse<>();
		
		pageResponse.setContent(userResponseModel);
		pageResponse.setPageNo(userEntityPage.getNumber());
		pageResponse.setPageSize(userEntityPage.getSize());
		pageResponse.setTotalElements(userEntityPage.getTotalElements());
		pageResponse.setTotalPages(userEntityPage.getTotalPages());
		pageResponse.setLast(userEntityPage.isLast());
		
		return pageResponse;
	}
	


	@Override
	public UserResponseModel getUser(String publicUserId) {
		
		UserEntity userEntity = userRepository.findByPublicUserId(publicUserId)
				.orElseThrow(()-> new ResourceNotFoundException("User", "publicUserId", publicUserId));
		
		UserResponseModel userResponse = modelMapper.map(userEntity, UserResponseModel.class);
		
		return userResponse;
	}

	@Override
	public UserResponseModel updateUser(String publicUserId, UserRequestModel userRequestModel) {

		UserEntity userEntity = userRepository.findByPublicUserId(publicUserId)
				.orElseThrow(()-> new ResourceNotFoundException("User", "publicUserId", publicUserId));
		
		if(userRequestModel.getFirstName() != null && userRequestModel.getFirstName().length() > 3) {
			
			userEntity.setFirstName(userRequestModel.getFirstName());
		}
		
		if(userRequestModel.getLastName() != null && userRequestModel.getLastName().length() > 3) {
			
			userEntity.setLastName(userRequestModel.getLastName());
		}
		
		if(userRequestModel.getPassword() != null && userRequestModel.getPassword().length() > 7) {
			userEntity.setPassword(passwordEncoder.encode(userRequestModel.getPassword()));
		}
		
		UserEntity updatedUserEntity = userRepository.save(userEntity);
		UserResponseModel updatedUserResponse = modelMapper.map(updatedUserEntity, UserResponseModel.class);
		
		return updatedUserResponse;
	}

	@Override
	public void deleteUser(String publicUserId) {

		UserEntity userEntity = userRepository.findByPublicUserId(publicUserId)
				.orElseThrow(()-> new ResourceNotFoundException("User", "publicUserId", publicUserId));

		userEntity.getRoles().forEach(role -> {
			if(role.getName().equals(UserRoles.ROLE_ADMIN.name())) {
				throw new BlogApiException("Users With ADMIN Role Cannot Be Deleted");
			}
		});
		
		userEntity.setRoles(null);
		
		userRepository.delete(userEntity);
		
	}

}
