package com.myblog.service.impl;

import static com.myblog.utils.UserRoles.ROLE_ADMIN;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.myblog.dto.request.PostRequestModel;
import com.myblog.dto.response.AllPostsResponseModel;
import com.myblog.dto.response.PageResponse;
import com.myblog.dto.response.PostResponseModel;
import com.myblog.entity.CategoryEntity;
import com.myblog.entity.PostEntity;
import com.myblog.entity.UserEntity;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.repository.CategoryRepository;
import com.myblog.repository.PostRepository;
import com.myblog.repository.UserRepository;
import com.myblog.security.UserPrincipal;
import com.myblog.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	private PostRepository postRepository;
	private ModelMapper modelMapper;
	private CategoryRepository categoryRepository;
	private UserRepository userRepository;
	
	@Autowired
	public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper,
			CategoryRepository categoryRepository, UserRepository userRepository) {
		
		this.postRepository = postRepository;
		this.modelMapper = modelMapper;
		this.categoryRepository = categoryRepository;
		this.userRepository = userRepository;
	}
	
	@Override
	public PostResponseModel createPost(PostRequestModel postRequestModel) {
		
		//Get current authenticated user to check if he has ADMIN role or he is the one who wrote this comment
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
		
		UserEntity userEntity = userRepository.findByEmail(authenticatedUser.getName())
				.orElseThrow(()-> new ResourceNotFoundException("User", "Email", authenticatedUser.getName()));
		
		PostEntity postEntity = new PostEntity();
		
		CategoryEntity categoryEntity = 
				categoryRepository.findById(postRequestModel.getCategoryId())
				.orElseThrow(()-> new ResourceNotFoundException
						("Category", "Categor ID", String.valueOf(postRequestModel.getCategoryId())));
		
		
		postEntity.setTitle(postRequestModel.getTitle());
		postEntity.setContent(postRequestModel.getContent());
		postEntity.setCategory(categoryEntity);
		postEntity.setDescription(postRequestModel.getDescription());
		postEntity.setUser(userEntity);
		postEntity.setCreationDate(new Date());
		
		postRepository.save(postEntity);
		
		PostResponseModel postResponse = modelMapper.map(postEntity, PostResponseModel.class);
		
		return postResponse;
	}

	@Override
	public PageResponse<AllPostsResponseModel> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
		
		if(pageNo > 0) {
			pageNo -= 1;
		}
		
		Sort sort = 
				sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		PageResponse<AllPostsResponseModel> pageResponse = new PageResponse<>();
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page <PostEntity> postEntityPages = postRepository.findAll(pageable);
	
		List<PostEntity> postEntity = postEntityPages.getContent();
		
		//Type typeList = new TypeToken<List<PostDto>>(){}.getType();
		
		//List<PostDto> responsePostDto = modelMapper.map(postEntity, typeList);
		
		List<AllPostsResponseModel> responsePostDto = postEntity.stream()
		.map(post -> new AllPostsResponseModel(post.getId(),
		
					post.getTitle(),
					post.getDescription(),
					post.getContent(),
					post.getCreationDate(),
					post.getCategory().getId())
				)
		.collect(Collectors.toList());
		
		
		pageResponse.setContent(responsePostDto);
		pageResponse.setPageNo(postEntityPages.getNumber()+1);
		pageResponse.setPageSize(postEntityPages.getSize());
		pageResponse.setTotalElements(postEntityPages.getTotalElements());
		pageResponse.setTotalPages(postEntityPages.getTotalPages());
		pageResponse.setLast(postEntityPages.isLast());
		
		return pageResponse;
	}

	@Override
	public PostResponseModel getPost(long postId) {

		PostEntity postEntity = postRepository.findById(postId)
				.orElseThrow(()-> new ResourceNotFoundException("Post","ID",String.valueOf(postId)));
		
		PostResponseModel postResponse = modelMapper.map(postEntity, PostResponseModel.class);
				
		return postResponse;
	}

	@Override
	public PageResponse<AllPostsResponseModel> getPostsCreatedByUser(String publicUserId, int pageNo, int pageSize,
					String sortBy, String sortDir) {
		if(pageNo > 0) {
			pageNo -= 1;
		}
		
		boolean isUserExists = userRepository.findByPublicUserId(publicUserId).isPresent();
		
		if(!isUserExists) {
			throw new ResourceNotFoundException("User", "Public ID", publicUserId);
		}
				
		
		Sort sort = 
				sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		PageResponse<AllPostsResponseModel> pageResponse = new PageResponse<>();
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page <PostEntity> postEntityPages = postRepository.findAllByUserPublicUserId(publicUserId, pageable);
	
		List<PostEntity> postEntity = postEntityPages.getContent();
		
		//Type typeList = new TypeToken<List<PostDto>>(){}.getType();
		
		//List<PostDto> responsePostDto = modelMapper.map(postEntity, typeList);
		
		List<AllPostsResponseModel> responsePostDto = postEntity.stream()
		.map(post -> new AllPostsResponseModel(post.getId(),
					post.getTitle(),
					post.getDescription(),
					post.getContent(),
					post.getCreationDate(),
					post.getCategory().getId()))
		.collect(Collectors.toList());
		
		
		pageResponse.setContent(responsePostDto);
		pageResponse.setPageNo(postEntityPages.getNumber()+1);
		pageResponse.setPageSize(postEntityPages.getSize());
		pageResponse.setTotalElements(postEntityPages.getTotalElements());
		pageResponse.setTotalPages(postEntityPages.getTotalPages());
		pageResponse.setLast(postEntityPages.isLast());
		
		return pageResponse;
	}

	@Override
	public PostResponseModel updatePost(long postId, PostRequestModel postRequestModel) {
		
		//Get current authenticated user to check if he has ADMIN role or he is the one who created this post
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
		
		
		UserPrincipal userPrincipal = (UserPrincipal) authenticatedUser.getPrincipal();

				
		PostEntity postEntity = postRepository.findById(postId)
				.orElseThrow(()-> new ResourceNotFoundException("Post","ID",String.valueOf(postId)));
		
		if(!(userPrincipal.getUserId() == postEntity.getUser().getId()) && 
				!userPrincipal.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN.name()))) {
			
			throw new AccessDeniedException(String
					.format("UNAUTHORIZED: User %s is not authorized to update this post", 
							authenticatedUser.getName()));
		}
		
		CategoryEntity categoryEntity = 
				categoryRepository.findById(postRequestModel.getCategoryId())
				.orElseThrow(()-> new ResourceNotFoundException
						("Category", "Categor ID", String.valueOf(postRequestModel.getCategoryId())));
		
		postEntity.setCategory(categoryEntity);
		postEntity.setContent(postRequestModel.getContent());
		postEntity.setTitle(postRequestModel.getTitle());
		postEntity.setDescription(postRequestModel.getDescription());
		
		
		PostEntity updatedPostEntity = postRepository.save(postEntity);
		
		PostResponseModel responsePostDto = modelMapper.map(updatedPostEntity, PostResponseModel.class);
				
		return responsePostDto;
	}

	@Override
	public void deletePost(long postId) {
		
		//Get current authenticated user to check if he has ADMIN role or he is the one who created this post
		Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
				
				
		UserPrincipal userPrincipal = (UserPrincipal) authenticatedUser.getPrincipal();

						
		PostEntity postEntity = postRepository.findById(postId)
				.orElseThrow(()-> new ResourceNotFoundException("Post","ID",String.valueOf(postId)));
				
		if(!(userPrincipal.getUserId() == postEntity.getUser().getId()) && 
				!userPrincipal.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN.name()))) {
					
			throw new AccessDeniedException(String
					.format("UNAUTHORIZED: User %s is not authorized to delete this post", 
							authenticatedUser.getName()));
		}
		
		postRepository.delete(postEntity);
		
	}
	
	@Override
	public PageResponse<AllPostsResponseModel> getAllPostsByCategory(long categoryId, int pageNo,
			int pageSize, String sortDir) {
		
		if(pageNo > 0) {
			pageNo -= 1;
		}
		
		Sort sort = 
				sortDir.equalsIgnoreCase("desc") ? Sort.by("id").descending() : Sort.by("id").ascending();
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page<PostEntity> postEntityPages = postRepository.findAllByCategoryId(categoryId, pageable);
		
		List<PostEntity> posts = postEntityPages.getContent();
		
		List<AllPostsResponseModel> postsDto = posts.stream()
				.map(post -> new AllPostsResponseModel(post.getId(),
				
						post.getTitle(),
						post.getDescription(),
						post.getContent(),
						post.getCreationDate(),
						post.getCategory().getId())
				)
				.collect(Collectors.toList());
		
		PageResponse<AllPostsResponseModel> pageResponse = new PageResponse<>();
		
		pageResponse.setContent(postsDto);
		pageResponse.setPageNo(postEntityPages.getNumber());
		pageResponse.setPageSize(postEntityPages.getSize());
		pageResponse.setTotalElements(postEntityPages.getTotalElements());
		pageResponse.setTotalPages(postEntityPages.getTotalPages());
		pageResponse.setLast(postEntityPages.isLast());
		
		return pageResponse;
	}

}
