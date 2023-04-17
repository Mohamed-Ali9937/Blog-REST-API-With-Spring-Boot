package com.myblog.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostRequestModel {
	
	@NotNull(message = "Post title cannot be empty")
	@Size(min = 10, max = 60)
	private String title;

	@NotNull(message = "Post decription cannot be empty")
	@Size(min = 10, max = 100)
	private String description;

	@NotNull(message = "Post content cannot be empty")
	private String content;
	
	@NotNull(message = "Post category cannot be empty")
	@Min(value = 1)
	private long categoryId;
	
	public PostRequestModel() {
		
	}

	public PostRequestModel(@NotNull(message = "Post title cannot be empty") @Size(min = 10, max = 60) String title,
			@NotNull(message = "Post decription cannot be empty") @Size(min = 10, max = 100) String description,
			@NotNull(message = "Post content cannot be empty") String content,
			@NotNull(message = "Post category cannot be empty") long categoryId) {
		this.title = title;
		this.description = description;
		this.content = content;
		this.categoryId = categoryId;
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

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
