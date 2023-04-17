package com.myblog.service;

import com.myblog.dto.request.PostRequestModel;
import com.myblog.dto.response.AllPostsResponseModel;
import com.myblog.dto.response.PageResponse;
import com.myblog.dto.response.PostResponseModel;

public interface PostService {
	PostResponseModel createPost(PostRequestModel postRequestModel);
	
	PageResponse <AllPostsResponseModel> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
	
	PostResponseModel getPost(long postId);
	
	PageResponse <AllPostsResponseModel>  getPostsCreatedByUser(String publicUserId,int pageNo, int pageSize,
								String sortBy, String sortDir);

	PostResponseModel updatePost(long postId, PostRequestModel postRequestModel);

	void deletePost(long postId);

	PageResponse<AllPostsResponseModel> getAllPostsByCategory(long categoryId, int pageNo, int pageSize, String sortDir);
}
