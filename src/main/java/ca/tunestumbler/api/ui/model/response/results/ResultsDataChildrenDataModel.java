package ca.tunestumbler.api.ui.model.response.results;

public class ResultsDataChildrenDataModel {
	private String subreddit;
	private String title;
	private int score;
	private long created;
	private String domain;
	private Boolean over_18;
	private Boolean spoiler;
	private String permalink;
	private Boolean stickied;
	private String url;
	private long created_utc;

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

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Boolean getOver_18() {
		return over_18;
	}

	public void setOver_18(Boolean over_18) {
		this.over_18 = over_18;
	}

	public Boolean getSpoiler() {
		return spoiler;
	}

	public void setSpoiler(Boolean spoiler) {
		this.spoiler = spoiler;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public Boolean getStickied() {
		return stickied;
	}

	public void setStickied(Boolean stickied) {
		this.stickied = stickied;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getCreated_utc() {
		return created_utc;
	}

	public void setCreated_utc(long created_utc) {
		this.created_utc = created_utc;
	}

}
