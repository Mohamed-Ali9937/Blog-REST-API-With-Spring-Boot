package com.myblog.dto.response;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;
public class AllPostsResponseModel extends RepresentationModel<AllPostsResponseModel>{
	private long id;

	private String title;

	private String description;

	private String content;

	private Date creationDate;
	
	private long categoryId;

	public AllPostsResponseModel() {

	}

	public AllPostsResponseModel(long id, String title, String description, String content, Date creationDate,
			long categoryId) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.content = content;
		this.creationDate = creationDate;
		this.categoryId = categoryId;
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

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

}
