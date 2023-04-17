package com.myblog.controller;

import static com.myblog.utils.OperationName.DELETE;
import static com.myblog.utils.OperationStatus.SUCCESS;
import static com.myblog.utils.PaginationConstants.DEFAULT_PAGE_NUMBER;
import static com.myblog.utils.PaginationConstants.DEFAULT_PAGE_SIZE;
import static com.myblog.utils.PaginationConstants.DEFAULT_SORT_BY;
import static com.myblog.utils.PaginationConstants.DEFAULT_SORT_DIRECTION;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myblog.dto.request.PostRequestModel;
import com.myblog.dto.response.AllPostsResponseModel;
import com.myblog.dto.response.OperationStatusResponseModel;
import com.myblog.dto.response.PageResponse;
import com.myblog.dto.response.PostResponseModel;
import com.myblog.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {
	
	private PostService postService;
	
	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@PostMapping
	public ResponseEntity<PostResponseModel> createPost(@Valid @RequestBody PostRequestModel postRequestModel) {
		
		PostResponseModel responsePostDto = postService.createPost(postRequestModel);
		
		return new ResponseEntity<>(responsePostDto, HttpStatus.CREATED);
	}
	
	@GetMapping
	public EntityModel<PageResponse<AllPostsResponseModel>> getAllPosts(
			@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
			@RequestParam(value = "sortDir" , defaultValue = DEFAULT_SORT_DIRECTION) String sortDir){
		
		PageResponse<AllPostsResponseModel> pageResponse = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
		
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
	
	@GetMapping("/{postId}")
	public ResponseEntity<EntityModel<PostResponseModel>> getPost(@PathVariable long postId){
		
		PostResponseModel responsePostDto = postService.getPost(postId);
		
		Link selfLink = 
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
				.getPost(postId)).withSelfRel();
		
		Link comments =
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class)
				.getCommentsByPost(postId)).withRel("Comments");
		
		Link cagtegory =
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class)
				.getCategory(responsePostDto.getCategoryId())).withRel("Category");
		
		Link user =
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getUser(responsePostDto.getPublicUserId())).withRel("User");
		
		return ResponseEntity.ok(EntityModel.of(responsePostDto, selfLink, comments, cagtegory, user));
	}

	@PutMapping("/{postId}")
	public ResponseEntity<EntityModel<PostResponseModel>> updatePost(@PathVariable long postId,
			@Valid @RequestBody PostRequestModel postRequestModel){
		
		PostResponseModel responsePostDto = postService.updatePost(postId, postRequestModel);
		
		Link selfLink = 
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
				.getPost(postId)).withSelfRel();
		
		return ResponseEntity.ok(EntityModel.of(responsePostDto, selfLink));
	}
	
	
	@DeleteMapping("/{postId}")
	public ResponseEntity<OperationStatusResponseModel> DeletePost(@PathVariable long postId){
		OperationStatusResponseModel operationStatus = new OperationStatusResponseModel();
			
		postService.deletePost(postId);
		operationStatus.setOperationName(DELETE.name());
		operationStatus.setOperationStatus(SUCCESS.name());
		return ResponseEntity.ok(operationStatus);
	}
	
	//creating a method using @InitBiner annotation to invoke every call to this controller
	//and remove all white spaces at the end the beginning of any String value to validate it
	//Note that @InitBinder make methods execute first whenever this controller is called
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		//to trim all white spaces at the end the beginning to null
		StringTrimmerEditor trimmer = new StringTrimmerEditor(true);
		
		//Create custom editor which will process the operation of trimming on any string values 
		dataBinder.registerCustomEditor(String.class, trimmer);
		
	}
}
