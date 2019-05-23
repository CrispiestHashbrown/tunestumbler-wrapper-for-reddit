package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class MultiredditDTO implements Serializable {
	private static final long serialVersionUID = -5037000521917475599L;
	private long id;
	private String multireddit;
	private String subreddit;
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
