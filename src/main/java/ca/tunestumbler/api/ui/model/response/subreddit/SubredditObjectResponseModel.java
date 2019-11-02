package ca.tunestumbler.api.ui.model.response.subreddit;

public class SubredditObjectResponseModel {
	private String subredditId;
	private String subreddit;
	private String afterId;
	private String beforeId;
	private String lastModified;

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

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

}
