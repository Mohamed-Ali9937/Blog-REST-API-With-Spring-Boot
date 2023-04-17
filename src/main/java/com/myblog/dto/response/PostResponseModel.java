package com.myblog.dto.response;

import java.util.Date;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.myblog.dto.request.CommentRequestModel;

public class PostResponseModel extends RepresentationModel<PostResponseModel> {

	private long id;

	private String title;

	private String description;

	private String content;

	private Date creationDate;
	
	private long categoryId;
	
	private String publicUserId;

	private List<CommentRequestModel> comments;

	public PostResponseModel() {

	}

	public PostResponseModel(long id, String title, String description, String content, Date creationDate) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.content = content;
		this.creationDate = creationDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getPublicUserId() {
		return publicUserId;
	}

	public void setPublicUserId(String publicUserId) {
		this.publicUserId = publicUserId;
	}

	public List<CommentRequestModel> getComments() {
		return comments;
	}

	public void setComments(List<CommentRequestModel> comments) {
		this.comments = comments;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

}
