package com.myblog.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myblog.dto.request.CommentRequestModel;
import com.myblog.dto.response.AllCommentsResponseModel;
import com.myblog.dto.response.CommentResponseModel;
import com.myblog.dto.response.OperationStatusResponseModel;
import com.myblog.service.CommentService;
import com.myblog.utils.OperationName;
import com.myblog.utils.OperationStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts/{postId}/comments")

public class CommentController {
	
	private CommentService commentService;
	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	//Add a new comment for a certain post
	@PostMapping
	public ResponseEntity<CommentResponseModel> createComment(@PathVariable long postId, 
			@Valid @RequestBody CommentRequestModel commentDto) {
				
		CommentResponseModel responseComment = commentService.createComment(postId, commentDto);
		
		return new ResponseEntity<>(responseComment, HttpStatus.CREATED);
	}
	
	//Get all comments for a certain post
	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<AllCommentsResponseModel>>> getCommentsByPost(
			@PathVariable Long postId){
		
		
		List<EntityModel<AllCommentsResponseModel>> comments = commentService.getAllCommentsByPost(postId).stream()
				.map(comment -> {
					Link selfLink =
							WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class)
							.getComment(postId, comment.getId())).withSelfRel();
					
					return EntityModel.of(comment, selfLink);
				}).collect(Collectors.toList());
		
		Link selfLink =
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class)
				.getCommentsByPost(postId)).withSelfRel();
		
		return ResponseEntity.ok(CollectionModel.of(comments, selfLink));
	}
	
	//Get a specific comment of a certain post using comment public id
	@GetMapping("/{commentId}")
	public ResponseEntity<EntityModel<CommentResponseModel>> getComment(@PathVariable long postId, 
			@PathVariable long commentId){
		
		CommentResponseModel responseComment = commentService.getComment(postId, commentId);
		
		Link selfLink =
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class)
				.getComment(postId, commentId)).withSelfRel();
		
		Link post =
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
				.getPost(postId)).withRel("Post");
		
		Link user =
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getUser(responseComment.getPublicUserId())).withRel("User");
		
		return ResponseEntity.ok(EntityModel.of(responseComment, selfLink, post, user));
	}
	
	//Update a specific comment 
	@PutMapping("/{commentId}")
	public ResponseEntity<EntityModel<CommentResponseModel>> updateComment(@PathVariable long postId,
			@PathVariable long commentId, @Valid @RequestBody CommentRequestModel commentDto){
		
		CommentResponseModel updatedResponseComment = 
				commentService.updateComment(postId, commentId, commentDto);
		
		Link selfLink =
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class)
				.getComment(postId, commentId)).withSelfRel();
		
		return ResponseEntity.ok(EntityModel.of(updatedResponseComment, selfLink));
	}
	
	//Delete a specific comment 
	@DeleteMapping("/{commentId}")
	public ResponseEntity<OperationStatusResponseModel> deleteComment(@PathVariable long postId,
			@PathVariable long commentId) {
		
		OperationStatusResponseModel operationStatus = new OperationStatusResponseModel();
		
		operationStatus.setOperationName(OperationName.DELETE.name());
		
		commentService.deleteComment(postId, commentId);
		
		operationStatus.setOperationStatus(OperationStatus.SUCCESS.name());
		
		return ResponseEntity.ok(operationStatus);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		//to trim all white spaces at the end the beginning to null
		StringTrimmerEditor trimmer = new StringTrimmerEditor(true);
		
		//Create custom editor which will process the operation of trimming on any string values 
		dataBinder.registerCustomEditor(String.class, trimmer);
		
	}
}
