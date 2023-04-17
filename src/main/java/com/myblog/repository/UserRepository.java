package com.myblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myblog.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	Optional <UserEntity> findByEmail(String email);
	
	Optional <UserEntity> findByPublicUserId(String publicUserId);
	
	boolean existsByEmail(String email);
}
