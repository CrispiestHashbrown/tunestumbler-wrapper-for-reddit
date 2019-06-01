package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class SubredditAggregateDTO implements Serializable {
	private static final long serialVersionUID = -41686199429484255L;
	private long id;
	private String subredditAggregateId;
	private UserDTO userDTO;
	private SubredditDTO subredditDTO;
	private MultiredditDTO multiredditDTO;
	private String datetime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubredditAggregateId() {
		return subredditAggregateId;
	}

	public void setSubredditAggregateId(String subredditAggregateId) {
		this.subredditAggregateId = subredditAggregateId;
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

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

}
