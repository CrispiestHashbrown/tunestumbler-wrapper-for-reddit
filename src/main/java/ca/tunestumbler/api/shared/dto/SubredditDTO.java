package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;
import java.util.List;

public class SubredditDTO implements Serializable {
	private static final long serialVersionUID = -8630017465738870844L;
	private long id;
	private String subredditId;
	private String subreddit;
	private UserDTO userDTO;
	private String lastModified;
	private List<AggregateDTO> aggregate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubredditId() {
		return subredditId;
	}

	public void setSubredditId(String subredditId) {
		this.subredditId = subredditId;
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
