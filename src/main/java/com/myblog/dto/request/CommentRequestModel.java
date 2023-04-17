package com.myblog.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentRequestModel {

	@NotNull(message = "Comment body cannot be empty")
	@Size(min = 1, max = 200)
	private String body;

	public CommentRequestModel() {

	}

	public CommentRequestModel(String body) {

		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
