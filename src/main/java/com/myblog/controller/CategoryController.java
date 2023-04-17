package com.myblog.controller;

import static com.myblog.utils.PaginationConstants.DEFAULT_PAGE_NUMBER;
import static com.myblog.utils.PaginationConstants.DEFAULT_PAGE_SIZE;
import static com.myblog.utils.PaginationConstants.DEFAULT_SORT_DIRECTION;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myblog.dto.CategoryDto;
import com.myblog.dto.response.AllPostsResponseModel;
import com.myblog.dto.response.OperationStatusResponseModel;
import com.myblog.dto.response.PageResponse;
import com.myblog.service.CategoryService;
import com.myblog.service.PostService;
import com.myblog.utils.OperationName;
import com.myblog.utils.OperationStatus;

@RestController
@RequestMapping("/api/categories")

public class CategoryController {
	
	private CategoryService categoryService;
	private PostService postService;
	
	@Autowired
	public CategoryController(CategoryService categoryService, PostService postService) {
		this.categoryService = categoryService;
		this.postService = postService;
	}

	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<EntityModel<CategoryDto>> createCategory(@RequestBody CategoryDto categoryDto){
		
		CategoryDto ResponseategoryDto = categoryService.createCategory(categoryDto);
		
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class)
				.getCategory(ResponseategoryDto.getId())).withSelfRel();
		
		return new ResponseEntity<>(EntityModel.of(ResponseategoryDto, selfLink), HttpStatus.CREATED);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<EntityModel<CategoryDto>> getCategory(@PathVariable long categoryId){
		
		CategoryDto ResponseategoryDto = categoryService.getCategory(categoryId);
		
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class)
				.getCategory(categoryId)).withSelfRel();
		
		Link posts = WebMvcLinkBuilder.linkTo(CategoryController.class)
				.slash(categoryId).slash("posts").withRel("Category-Posts");
		
		return ResponseEntity.ok(EntityModel.of(ResponseategoryDto, selfLink, posts));
	}
	
	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<CategoryDto>>> getAllCategories(){
		
		List<EntityModel<CategoryDto>> categories = categoryService.getAllCategories().stream()
				.map(category -> {
					Link selfLink = 
							WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class)
							.getCategory(category.getId())).withSelfRel();
					
					Link postsLink = 
							WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class)
							.getCategory(category.getId())).slash("posts").withRel("Category-Posts");
					
					return EntityModel.of(category, selfLink, postsLink);
				}).collect(Collectors.toList());
		
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class)
				.getAllCategories()).withSelfRel();
		
		return ResponseEntity.ok(CollectionModel.of(categories, selfLink));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{categoryId}")
	public ResponseEntity<EntityModel<CategoryDto>> updateCategory(@PathVariable long categoryId,
			@RequestBody CategoryDto categoryDto){
		
		CategoryDto updatedCategoryDto = categoryService.updateCategory(categoryDto, categoryId);
		
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class)
				.getCategory(categoryId)).withSelfRel();
		
		return ResponseEntity.ok(EntityModel.of(updatedCategoryDto, selfLink));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("{categoryId}")
	public ResponseEntity<OperationStatusResponseModel> deleteCategory(@PathVariable long categoryId){
		
		OperationStatusResponseModel operationStatus = new OperationStatusResponseModel();
		
		operationStatus.setOperationName(OperationName.DELETE.name());
		
		categoryService.deleteCategory(categoryId);
		
		operationStatus.setOperationStatus(OperationStatus.SUCCESS.name());
		
		return ResponseEntity.ok(operationStatus);
				
	}
	
	@GetMapping("/{categoryId}/posts")
	public ResponseEntity<EntityModel<PageResponse<AllPostsResponseModel>>> getAllCategoryPosts(
			@PathVariable long categoryId,
			@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(value = "pageNo", defaultValue = DEFAULT_SORT_DIRECTION) String sotrDit){
		
		PageResponse<AllPostsResponseModel> postPages = 
				postService.getAllPostsByCategory(categoryId, pageNo, pageSize, sotrDit);
		
		postPages.getContent().forEach(post -> {
			Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
					.getPost(post.getId())).withSelfRel();
			post.add(selfLink);
		});
		
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class)
				.getAllCategoryPosts(categoryId, pageNo, pageSize, sotrDit)).withSelfRel();
		
		
		return ResponseEntity.ok(EntityModel.of(postPages, selfLink));
	}
}
