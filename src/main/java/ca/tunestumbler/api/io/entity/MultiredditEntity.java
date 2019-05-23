package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class MultiredditEntity implements Serializable {
	private static final long serialVersionUID = 9178020527307904668L;

	@Id
	@GeneratedValue
	private long id;

	// Full path of multireddit
	@Column(nullable = false)
	private String multireddit;

	@Column(nullable = false, length = 21)
	private String subreddit;

	@Column(nullable = false)
	private String userId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
