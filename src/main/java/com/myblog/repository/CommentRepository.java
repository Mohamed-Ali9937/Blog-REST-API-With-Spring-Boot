package com.myblog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myblog.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
	
	/*Custom query method to retrieve the comments using publicPostId
	 Note that before writing the field that we want to use for retrieving data from database
	 we should first write the property name of the target Entity that owns this field that is used in the
	 Entity which is processing data retrieving like CommentEntity in our case
	 as this example property name of the type PostEntity in the CommentEntity is 'post'
	 then we should use the full path to use the Id property as follows: PosttId
	*/
	
	List<CommentEntity> findAllByPostId(long postId);
	
}