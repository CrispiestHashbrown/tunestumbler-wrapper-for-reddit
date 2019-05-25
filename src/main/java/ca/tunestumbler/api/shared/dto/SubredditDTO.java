package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class SubredditDTO implements Serializable {
	private static final long serialVersionUID = -8630017465738870844L;
	private long id;
	private String subreddit;
	private UserDTO userDTO;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubreddit() {
		return subreddit;
	}

	public void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

}
