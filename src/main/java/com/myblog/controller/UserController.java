package com.myblog.controller;

import static com.myblog.utils.PaginationConstants.DEFAULT_PAGE_NUMBER;
import static com.myblog.utils.PaginationConstants.DEFAULT_PAGE_SIZE;
import static com.myblog.utils.PaginationConstants.DEFAULT_SORT_BY;
import static com.myblog.utils.PaginationConstants.DEFAULT_SORT_DIRECTION;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myblog.dto.request.UserRequestModel;
import com.myblog.dto.response.AllPostsResponseModel;
import com.myblog.dto.response.OperationStatusResponseModel;
import com.myblog.dto.response.PageResponse;
import com.myblog.dto.response.UserResponseModel;
import com.myblog.service.PostService;
import com.myblog.service.UserService;
import com.myblog.utils.OperationName;
import com.myblog.utils.OperationStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")

public class UserController {

	private UserService userService;
	private PostService postService;
	
	@Autowired
	public UserController(UserService userService, PostService postService) {
		this.userService = userService;
		this.postService = postService;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<EntityModel<PageResponse<UserResponseModel>>> getAllUsers(
			@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
			@RequestParam(value = "sortDir" , defaultValue = DEFAULT_SORT_DIRECTION) String sortDir){
		
		PageResponse<UserResponseModel> pageResponse = 
				userService.getUsers(pageNo, pageSize, sortBy, sortDir);
		
		pageResponse.getContent().forEach(user -> {
					Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
							.getUser(user.getPublicUserId())).withSelfRel();
					user.add(selfLink);
				});
		
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getAllUsers(pageNo, pageSize, sortBy, sortDir)).withSelfRel();
		
		return ResponseEntity.ok(EntityModel.of(pageResponse, selfLink));
	}
	
	@GetMapping("/{publicUserId}")
	public ResponseEntity<EntityModel<UserResponseModel>> getUser(@PathVariable String publicUserId) {
		
		UserResponseModel userResponse = userService.getUser(publicUserId);
				
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getUser(publicUserId)).withSelfRel();
		
		return ResponseEntity.ok(EntityModel.of(userResponse, selfLink));
	}

	@PreAuthorize("hasRole('ADMIN') or principal.publicUserId == #publicUserId")
	@PutMapping("/{publicUserId}")
	public ResponseEntity<EntityModel<UserResponseModel>> updateUser(@PathVariable String publicUserId,
			@Valid @RequestBody UserRequestModel userRequestModel) {
		
		UserResponseModel updatedUser = userService.updateUser(publicUserId, userRequestModel);
		
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getUser(publicUserId)).withSelfRel();
		
		return ResponseEntity.ok(EntityModel.of(updatedUser, selfLink));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{publicUserId}")
	public ResponseEntity<OperationStatusResponseModel> deleteUser(@PathVariable String publicUserId){
		
		OperationStatusResponseModel operationStatus = new OperationStatusResponseModel();
		
		operationStatus.setOperationName(OperationName.DELETE.name());
		
		userService.deleteUser(publicUserId);
		
		operationStatus.setOperationStatus(OperationStatus.SUCCESS.name());
		
		return ResponseEntity.ok(operationStatus);
		
	}
	
	
	@GetMapping("/{publicUserId}/posts")
	public EntityModel<PageResponse<AllPostsResponseModel>> getPostsCreatedByUser(@PathVariable String publicUserId,
			@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
			@RequestParam(value = "sortDir" , defaultValue = DEFAULT_SORT_DIRECTION) String sortDir){
		
		PageResponse<AllPostsResponseModel> pageResponse = 
				postService.getPostsCreatedByUser(publicUserId, pageNo, pageSize, sortBy, sortDir);
		
		pageResponse.getContent().forEach(post -> {
					Link selfLink =
							WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
							.getPost(post.getId())).withSelfRel();
					
					post.add(selfLink);
				});
		
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
				.getAllPosts(pageNo,pageSize,sortBy,sortDir)).withSelfRel();
		
		return EntityModel.of(pageResponse, selfLink);
	}
	
	
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		//to trim all white spaces at the end the beginning to null
		StringTrimmerEditor trimmer = new StringTrimmerEditor(true);
		
		//Create custom editor which will process the operation of trimming on any string values 
		dataBinder.registerCustomEditor(String.class, trimmer);
		
	}
}
