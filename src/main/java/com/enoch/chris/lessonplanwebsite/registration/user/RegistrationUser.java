

package com.enoch.chris.lessonplanwebsite.registration.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enoch.chris.lessonplanwebsite.validation.FieldMatch;
import com.enoch.chris.lessonplanwebsite.validation.ValidEmail;
import com.enoch.chris.lessonplanwebsite.validation.ValidPassword;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must match")
})
public class RegistrationUser {

	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	private String userName;

	//@NotNull(message = "")
	//@Size(min = 1)
	@NotNull(message = "")
	@Size(min = 1, message = "")
	@ValidPassword
	private String password;
	
	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	private String matchingPassword;

	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	private String firstName;

	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	private String lastName;

	@ValidEmail
	@NotNull(message = "Your email is required")
	@Size(min = 1, message = "Your email is required")
	private String email;

	public RegistrationUser() {

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}