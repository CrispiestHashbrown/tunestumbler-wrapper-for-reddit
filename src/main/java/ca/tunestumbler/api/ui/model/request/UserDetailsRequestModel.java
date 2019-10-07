package ca.tunestumbler.api.ui.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDetailsRequestModel {

	@NotNull(message = "Email cannot be null")
	@Size(min = 3, max = 254, message = "Email must have length of 3-254 characters")
	private String email;

	@NotNull(message = "Password cannot be null")
	@Size(min = 8, max = 30, message = "Password must have length of 8-30 characters")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
