package com.myblog.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.myblog.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
	
	boolean existsByName(String name);
}
