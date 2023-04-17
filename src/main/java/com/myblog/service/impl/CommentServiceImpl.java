package com.myblog.service.impl;

import static com.myblog.utils.UserRoles.ROLE_ADMIN;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.myblog.dto.request.CommentRequestModel;
import com.myblog.dto.response.AllCommentsResponseModel;
import com.myblog.dto.response.CommentResponseModel;
import com.myblog.entity.CommentEntity;
import com.myblog.entity.PostEntity;
import com.myblog.entity.UserEntity;
import com.myblog.exception.BlogApiException;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.repository.CommentRepository;
import com.myblog.repository.PostRepository;
import com.myblog.repository.UserRepository;
import com.myblog.service.CommentService;;

@Service
public class CommentServiceImpl implements CommentService {

	private CommentRepository commentRepository;
	private PostRepository postRepository;
	private ModelMapper modelMapper;
	private UserRepository userRepository;
	
	@Autowired
	public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
			ModelMapper modelMapper, UserRepository userRepository) {
		
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
	}
	
	//Add a new comment for a certain post
	@Override
	public CommentResponseModel createComment(long postId, CommentRequestModel commentDto) {
		
		//Get current authenticated user to check if he has ADMIN role or he is the one who wrote this comment
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
		
		UserEntity userEntity = userRepository.findByEmail(authenticatedUser.getName())
				.orElseThrow(()-> new ResourceNotFoundException("User", "Public ID", authenticatedUser.getName()));
		
		//Retrieve the post from the database
		PostEntity postEntity = postRepository.findById(postId)
				.orElseThrow(()-> new ResourceNotFoundException("Post", "ID", String.valueOf(postId)));
		
		CommentEntity commentEntity = new CommentEntity();
		
		//set body
		commentEntity.setBody(commentDto.getBody());
		
		//Set Comment creation date	
		commentEntity.setDate(new Date());
		
		//set user name
		commentEntity.setName(String.format("%s %s", userEntity.getFirstName(), userEntity.getLastName()));
		
	    //set email
		commentEntity.setEmail(userEntity.getEmail());
		
		//Set User Details to the comment to help spring data determine which user wrote this comment
		commentEntity.setUser(userEntity);
		
		//Set post Details to the comment to help spring data determine which post this comment is for
		commentEntity.setPost(postEntity);
		
		//Save the comment into the database
		CommentEntity savedCommentEntity = commentRepository.save(commentEntity);
		
		//Map commentEntity to commentDto
		CommentResponseModel responseComment = modelMapper.map(savedCommentEntity, CommentResponseModel.class);
		
		return responseComment;
	}

	//Get all comments for a certain post
	@Override
	public List<AllCommentsResponseModel> getAllCommentsByPost(long postId){
		

		List<CommentEntity> commentsEntity = commentRepository.findAllByPostId(postId);
		
		//Creating typeList to map List of CommentEntity to list of CommentDto
		Type typeList = new TypeToken<List<AllCommentsResponseModel>>(){}.getType();
		
		List<AllCommentsResponseModel> responseComment = modelMapper.map(commentsEntity, typeList);
				
		return responseComment;
	}

	@Override
	public CommentResponseModel getComment(long postId, long commentId) {
		
		//Retrieve the post from the database
				PostEntity postEntity = postRepository.findById(postId)
						.orElseThrow(()-> new ResourceNotFoundException("Post", "ID", String.valueOf(postId)));
				
		//Retrieve the comment from the database
		CommentEntity commentEntity = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "ID", String.valueOf(commentId)));
		
		//Check if this comment is belong to the specified post
		if(commentEntity.getPost().getId() != postEntity.getId()) {
			throw new BlogApiException("There is no such comment for this post");
		}
		
		CommentResponseModel responseCommentDto = modelMapper.map(commentEntity, CommentResponseModel.class);
		
		return responseCommentDto;
	}

	@Override
	public CommentResponseModel updateComment(long postId, long commentId,
			CommentRequestModel commentRequestModel) {
		
		//Get current authenticated user to check if he has ADMIN role or he is the one who wrote this comment
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
		
		//Retrieve the comment from the database
		CommentEntity commentEntity = commentRepository.findById(commentId)
		.orElseThrow(() -> new ResourceNotFoundException("Comment", "ID", String.valueOf(commentId)));
		
		if(!authenticatedUser.getName().equals(commentEntity.getEmail()) && 
				!authenticatedUser.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN.name()))) {
			
			throw new AccessDeniedException(String
					.format("UNAUTHORIZED: User %s is not authorized to update this comment", 
							authenticatedUser.getName()));
		}
		
		//Retrieve the post from the database
		PostEntity postEntity = postRepository.findById(postId)
				.orElseThrow(()-> new ResourceNotFoundException("Post", "ID", String.valueOf(postId)));
		
		//Check if this comment is belong to the specified post
		if(commentEntity.getPost().getId() != postEntity.getId()) {
			throw new BlogApiException("There is no such comment for this post");
		}
		
		//update the comment
		commentEntity.setBody(commentRequestModel.getBody());
		
		//save the updated comment
		CommentEntity updatedCommentEntity = commentRepository.save(commentEntity);
		
		//Map the updated CommentEntity into CommentDto
		CommentResponseModel responseComment = modelMapper.map(updatedCommentEntity, CommentResponseModel.class);
		
		return responseComment;
	}

	@Override
	public void deleteComment(long postId, long commentId) {
		
		//Retrieve the comment from the database
		CommentEntity commentEntity = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "ID", String.valueOf(commentId)));
		
		//Get current authenticated user to check if he has ADMIN role or he is the one who wrote this comment
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();

		if(!authenticatedUser.getName().equals(commentEntity.getEmail()) && 
				!authenticatedUser.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN.name()))) {
					
			throw new AccessDeniedException(String
					.format("UNAUTHORIZED: User %s is not authorized to delete this comment", 
							authenticatedUser.getName()));
		}
		
		//Retrieve the post from the database
		PostEntity postEntity = postRepository.findById(postId)
				.orElseThrow(()-> new ResourceNotFoundException("Post", "ID", String.valueOf(postId)));
	
		//Check if this comment is belong to the specified post
		if(commentEntity.getPost().getId() != postEntity.getId()) {
			throw new BlogApiException("There is no such comment for this post");
		}
		
		commentRepository.delete(commentEntity);
	}


}
