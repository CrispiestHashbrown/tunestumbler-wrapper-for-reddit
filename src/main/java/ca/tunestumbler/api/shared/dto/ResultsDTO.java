package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

// Aggregate Results before filters are applied
public class ResultsDTO implements Serializable {
	private static final long serialVersionUID = 1103952203234348723L;
	private long id;
	private String resultsId;
	private SubredditAggregateDTO subredditAggregateDTO;
	private String resultsUrl;
	private String subreddit;
	private String title;
	private String score;
	private String created;
	private String createdUtc;
	private String domain;
	private String nsfw;
	private String spoiler;
	private String permalink;
	private String stickied;
	private String mediaUrl;
	private String mediaEmbedId;
	private String secureMediaId;
	private String secureMediaEmbedId;
	private String mediaId;
	private String beforeResultsId;
	private String afterResultsId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getResultsId() {
		return resultsId;
	}

	public void setResultsId(String resultsId) {
		this.resultsId = resultsId;
	}

	public SubredditAggregateDTO getSubredditAggregateDTO() {
		return subredditAggregateDTO;
	}

	public void setSubredditAggregateDTO(SubredditAggregateDTO subredditAggregateDTO) {
		this.subredditAggregateDTO = subredditAggregateDTO;
	}

	public String getResultsUrl() {
		return resultsUrl;
	}

	public void setResultsUrl(String resultsUrl) {
		this.resultsUrl = resultsUrl;
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

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getCreatedUtc() {
		return createdUtc;
	}

	public void setCreatedUtc(String createdUtc) {
		this.createdUtc = createdUtc;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getNsfw() {
		return nsfw;
	}

	public void setNsfw(String nsfw) {
		this.nsfw = nsfw;
	}

	public String getSpoiler() {
		return spoiler;
	}

	public void setSpoiler(String spoiler) {
		this.spoiler = spoiler;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getStickied() {
		return stickied;
	}

	public void setStickied(String stickied) {
		this.stickied = stickied;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public String getMediaEmbedId() {
		return mediaEmbedId;
	}

	public void setMediaEmbedId(String mediaEmbedId) {
		this.mediaEmbedId = mediaEmbedId;
	}

	public String getSecureMediaId() {
		return secureMediaId;
	}

	public void setSecureMediaId(String secureMediaId) {
		this.secureMediaId = secureMediaId;
	}

	public String getSecureMediaEmbedId() {
		return secureMediaEmbedId;
	}

	public void setSecureMediaEmbedId(String secureMediaEmbedId) {
		this.secureMediaEmbedId = secureMediaEmbedId;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getBeforeResultsId() {
		return beforeResultsId;
	}

	public void setBeforeResultsId(String beforeResultsId) {
		this.beforeResultsId = beforeResultsId;
	}

	public String getAfterResultsId() {
		return afterResultsId;
	}

	public void setAfterResultsId(String afterResultsId) {
		this.afterResultsId = afterResultsId;
	}

}
