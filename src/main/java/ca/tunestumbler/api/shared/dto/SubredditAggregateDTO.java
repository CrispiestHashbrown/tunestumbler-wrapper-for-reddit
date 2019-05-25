package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class SubredditAggregateDTO implements Serializable {
	private static final long serialVersionUID = -41686199429484255L;
	private long id;
	private UserDTO userDTO;
	private SubredditDTO subredditDTO;
	private MultiredditDTO multiredditDTO;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public SubredditDTO getSubredditDTO() {
		return subredditDTO;
	}

	public void setSubredditDTO(SubredditDTO subredditDTO) {
		this.subredditDTO = subredditDTO;
	}

	public MultiredditDTO getMultiredditDTO() {
		return multiredditDTO;
	}

	public void setMultiredditDTO(MultiredditDTO multiredditDTO) {
		this.multiredditDTO = multiredditDTO;
	}

}
