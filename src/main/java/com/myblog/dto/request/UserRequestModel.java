package com.myblog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserRequestModel {
	
	@Email(message = "Email must be a well-formed, like : name@blog.com")
	private String email;
		
	
	@Size(min = 2, max = 30)
	private String firstName;

	@Size(min = 2, max = 30)
	private String lastName;
	
	@Size(min = 7, max = 30)
	private String password;
	
	public UserRequestModel() {
		
	}

	public UserRequestModel(String email, String firstName, String lastName, String password) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
