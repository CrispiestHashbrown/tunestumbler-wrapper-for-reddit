package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;
import java.util.List;

import ca.tunestumbler.api.io.entity.FiltersEntity;

public class AggregateDTO implements Serializable {
	private static final long serialVersionUID = -41686199429484255L;
	private long id;
	private String aggregateId;
	private UserDTO userDTO;
	private String userId;
	private String subredditId;
	private String subreddit;
	private Boolean isSubredditAdded = false;
	private String multiredditId;
	private String multireddit;
	private long startId;
	private String lastModified;
	private List<FiltersEntity> filters;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAggregateId() {
		return aggregateId;
	}

	public void setAggregateId(String aggregateId) {
		this.aggregateId = aggregateId;
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

	public Boolean getIsSubredditAdded() {
		return isSubredditAdded;
	}

	public void setIsSubredditAdded(Boolean isSubredditAdded) {
		this.isSubredditAdded = isSubredditAdded;
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

	public long getStartId() {
		return startId;
	}

	public void setStartId(long startId) {
		this.startId = startId;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public List<FiltersEntity> getFilters() {
		return filters;
	}

	public void setFilters(List<FiltersEntity> filters) {
		this.filters = filters;
	}

}
