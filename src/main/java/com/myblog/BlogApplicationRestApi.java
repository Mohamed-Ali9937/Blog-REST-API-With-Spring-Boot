package com.myblog;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.myblog.entity.RoleEntity;
import com.myblog.entity.UserEntity;
import com.myblog.repository.RoleRepository;
import com.myblog.repository.UserRepository;
import com.myblog.utils.UserRoles;


@SpringBootApplication
public class BlogApplicationRestApi implements CommandLineRunner {

	private RoleRepository roleRepository;
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public BlogApplicationRestApi(RoleRepository roleRepository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BlogApplicationRestApi.class, args);
	}

	@Transactional
	@Override
	public void run(String... args) throws Exception {		
		
		RoleEntity admin = new RoleEntity();
		admin.setName(UserRoles.ROLE_ADMIN.name());
		
		boolean isAdminRoleExists = roleRepository.existsByName(admin.getName());
		
		if(!isAdminRoleExists) {
			
			RoleEntity adminRole = roleRepository.save(admin);
			
			boolean isAdminExists = userRepository.existsByEmail("admin@blog.com");
			
			if(!isAdminExists) {
				UserEntity adminUser = new UserEntity();
								
				Set<RoleEntity> roles = new HashSet<>();
				roles.add(adminRole);
				
				adminUser.setPublicUserId(UUID.randomUUID().toString());
				adminUser.setEmail("admin@blog.com");
				adminUser.setPassword(passwordEncoder.encode("admin"));
				adminUser.setFirstName("admin");
				adminUser.setLastName("admin");
				adminUser.setRoles(roles);
				
				userRepository.save(adminUser);
			}
		}
		
		RoleEntity user = new RoleEntity();
		user.setName(UserRoles.ROLE_USER.name());
		
		boolean isUserRoleExists = roleRepository.existsByName(user.getName());
				
		if(!isUserRoleExists) {
			
			roleRepository.save(user);
		}
		
		
	}

}
