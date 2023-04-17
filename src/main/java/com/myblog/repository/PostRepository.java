package com.myblog.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myblog.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
	
	Page<PostEntity> findAllByCategoryId(long categoryId, Pageable pageable);
	
	Page<PostEntity> findAllByUserPublicUserId(String publicUserId, Pageable pageable);
}
