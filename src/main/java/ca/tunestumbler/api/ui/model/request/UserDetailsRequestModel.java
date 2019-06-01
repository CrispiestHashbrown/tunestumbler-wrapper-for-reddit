package ca.tunestumbler.api.ui.model.request;

public class UserDetailsRequestModel {
	private String email;
	private String password;
	private String redditAccountName;

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

	public String getRedditAccount() {
		return redditAccountName;
	}

	public void setRedditAccount(String redditAccount) {
		this.redditAccountName = redditAccount;
	}

}
