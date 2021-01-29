package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class SubredditDTO implements Serializable {
	private static final long serialVersionUID = -8630017465738870844L;
	private String subredditId;
	private String subreddit;
	private String afterId;
	private String beforeId;

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

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public String getBeforeId() {
		return beforeId;
	}

	public void setBeforeId(String beforeId) {
		this.beforeId = beforeId;
	}
}
