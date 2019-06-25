package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;
import java.util.List;

public class MultiredditDTO implements Serializable {
	private static final long serialVersionUID = -5037000521917475599L;
	private long id;
	private String multiredditId;
	private String multireddit;
	private String subreddit;
	private UserDTO userDTO;
	private String userId;
	private String lastModified;
	private List<AggregateDTO> aggregate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMultiredditId() {
		return multiredditId;
	}

	public void setMultiredditId(String multiredditId) {
		this.multiredditId = multiredditId;
	}

	public String getMultireddit() {
		return multireddit;
	}

	public void setMultireddit(String multireddit) {
		this.multireddit = multireddit;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public List<AggregateDTO> getAggregate() {
		return aggregate;
	}

	public void setAggregate(List<AggregateDTO> aggregate) {
		this.aggregate = aggregate;
	}

}
