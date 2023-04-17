package com.myblog.service;

import java.util.List;

import com.myblog.dto.request.CommentRequestModel;
import com.myblog.dto.response.AllCommentsResponseModel;
import com.myblog.dto.response.CommentResponseModel;

public interface CommentService {
	CommentResponseModel createComment(long postId, CommentRequestModel commentDto);

	List<AllCommentsResponseModel> getAllCommentsByPost(long postId);
	
	CommentResponseModel getComment(long postId, long commentId);
	
	CommentResponseModel updateComment(long postId, long commentId, CommentRequestModel commentDto);
	
	void deleteComment(long postId, long commentId);

}
