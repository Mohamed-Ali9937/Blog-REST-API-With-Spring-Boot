package com.myblog.dto.response;

public class RegisterResponseModel {
	private String publicUserId;

	private String email;

	private String firstName;

	private String lastName;
	
	public RegisterResponseModel() {
		
	}

	public RegisterResponseModel(String publicUserId, String email, String firstName, String lastName) {
		this.publicUserId = publicUserId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getPublicUserId() {
		return publicUserId;
	}

	public void setPublicUserId(String publicUserId) {
		this.publicUserId = publicUserId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
}
