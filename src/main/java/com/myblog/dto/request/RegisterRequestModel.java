package com.myblog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequestModel {
	
	@NotNull(message = "Email cannot be Empty")
	@Email(message = "Email must be a well-formed, like : name@blog.com")
	private String email;
		
	@NotNull(message = "First name cannot be Empty")
	private String firstName;

	@NotNull(message = "Last name cannot be Empty")
	private String lastName;
	
	@Size(min = 7, max = 30)
	@NotNull(message = "password cannot be Empty")
	private String password;
	public RegisterRequestModel() {
		
	}

	public RegisterRequestModel(String password, String email, String firstName, String lastName) {
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
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
	
	
}
