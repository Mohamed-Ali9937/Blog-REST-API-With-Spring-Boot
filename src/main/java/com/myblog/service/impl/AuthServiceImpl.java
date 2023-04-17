package com.myblog.service.impl;

import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myblog.dto.request.LoginRequestModel;
import com.myblog.dto.request.RegisterRequestModel;
import com.myblog.dto.response.RegisterResponseModel;
import com.myblog.entity.RoleEntity;
import com.myblog.entity.UserEntity;
import com.myblog.exception.BlogApiException;
import com.myblog.repository.RoleRepository;
import com.myblog.repository.UserRepository;
import com.myblog.security.JwtTokenUtils;
import com.myblog.service.AuthService;
import com.myblog.utils.UserRoles;

@Service
public class AuthServiceImpl implements AuthService {

	private AuthenticationManager authenticationManager;
	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private ModelMapper modelMapper;
	private JwtTokenUtils jwtTokenUtils;
	
	@Autowired
	public AuthServiceImpl(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
			UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper,
			JwtTokenUtils jwtTokenUtils) {

		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
		this.jwtTokenUtils = jwtTokenUtils;
	}

	@Override
	public String login(LoginRequestModel loginRequestModel) {

		Authentication authenticationToken = 
				new UsernamePasswordAuthenticationToken(loginRequestModel.getEmail(),
				loginRequestModel.getPassword());

		Authentication auth = authenticationManager.authenticate(authenticationToken);

		SecurityContextHolder.getContext().setAuthentication(auth);
		
		String jwtAuthorizationToken = jwtTokenUtils.generateJwtToken(loginRequestModel.getEmail());
		
		return jwtAuthorizationToken;
	}

	@Override
	public RegisterResponseModel register(RegisterRequestModel registerRequestModel) {
		
		UserEntity userEntity = modelMapper.map(registerRequestModel, UserEntity.class);
		
		boolean isExist = userRepository.existsByEmail(userEntity.getEmail());
		
		if(isExist) {
			throw new BlogApiException(String.format("This email: %s already exists", userEntity.getEmail()));
		}
		
		userEntity.setPublicUserId(UUID.randomUUID().toString());
		
		userEntity.setPassword(passwordEncoder.encode(registerRequestModel.getPassword()));
		
		RoleEntity role = roleRepository.findByName(UserRoles.ROLE_USER.name())
				.orElseThrow(()-> new BlogApiException(String.format
						("There is no role found in the database with name: %s", UserRoles.ROLE_USER.name())));
		
		userEntity.setRoles(Set.of(role));
		
		UserEntity savedUser = userRepository.save(userEntity);
		
		RegisterResponseModel userResponse = modelMapper.map(savedUser, RegisterResponseModel.class);
		
		return userResponse;
	}

}
