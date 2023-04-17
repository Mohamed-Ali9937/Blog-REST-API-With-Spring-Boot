package com.myblog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myblog.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
	
	Optional <RoleEntity> findByName(String name);
	
	boolean existsByName(String name);
}
