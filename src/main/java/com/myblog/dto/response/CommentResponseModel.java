package com.myblog.dto.response;

import java.util.Date;


public class CommentResponseModel {

	private long id;

	private String name;

	private String email;

	private String body;

	private Date date;
	
	private String publicUserId;
	
	public CommentResponseModel() {

	}

	public CommentResponseModel(long id, String name, String email, String body, Date date,
			String publicUserId) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.body = body;
		this.date = date;
		this.publicUserId = publicUserId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPublicUserId() {
		return publicUserId;
	}

	public void setPublicUserId(String publicUserId) {
		this.publicUserId = publicUserId;
	}

}
