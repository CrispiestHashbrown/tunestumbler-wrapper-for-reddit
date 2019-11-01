package ca.tunestumbler.api.ui.model.response.results;

public class ResultsObjectResponseModel {
	private String resultsId;
	private String userId;
	private String subreddit;
	private String title;
	private int score;
	private long created;
	private long createdUtc;
	private String domain;
	private Boolean isNsfw;
	private Boolean isSpoiler;
	private int comments;
	private String permalink;
	private Boolean isStickied;
	private String url;

	public String getResultsId() {
		return resultsId;
	}

	public void setResultsId(String resultsId) {
		this.resultsId = resultsId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSubreddit() {
		return subreddit;
	}

	public void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public long getCreatedUtc() {
		return createdUtc;
	}

	public void setCreatedUtc(long createdUtc) {
		this.createdUtc = createdUtc;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Boolean getIsNsfw() {
		return isNsfw;
	}

	public void setIsNsfw(Boolean isNsfw) {
		this.isNsfw = isNsfw;
	}

	public Boolean getIsSpoiler() {
		return isSpoiler;
	}

	public void setIsSpoiler(Boolean isSpoiler) {
		this.isSpoiler = isSpoiler;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public Boolean getIsStickied() {
		return isStickied;
	}

	public void setIsStickied(Boolean isStickied) {
		this.isStickied = isStickied;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
