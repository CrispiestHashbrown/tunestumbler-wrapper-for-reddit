package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ca.tunestumbler.api.shared.dto.SubredditAggregateDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

@Entity(name = "result")
public class ResultsEntity implements Serializable {
	private static final long serialVersionUID = -8505575230126284466L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	private String resultsId;

	@ManyToOne
	@JoinColumn(name = "subreddit_aggregate_id")
	private SubredditAggregateDTO subredditAggregateDTO;

	@ManyToOne
	@JoinColumn(name = "users_id")
	private UserDTO userDTO;

	@Column(nullable = false)
	private String resultsUrl;

	@Column(nullable = false, length = 21)
	private String subreddit;

	@Column(nullable = false, length = 300)
	private String title;

	@Column(nullable = false)
	private int score;

	@Column(nullable = false)
	private String created;

	@Column(nullable = false)
	private String createdUtc;

	@Column(length = 15)
	private String domain;

	@Column(nullable = false)
	private Boolean nsfw;

	@Column(nullable = false)
	private String spoiler;

	@Column(nullable = false)
	private String permalink;

	@Column(nullable = false)
	private String stickied;

	@Column
	private String mediaUrl;

	@Column
	private String mediaEmbedId;

	@Column
	private String secureMediaId;

	@Column
	private String secureMediaEmbedId;

	@Column
	private String mediaId;

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

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
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

	public Boolean getNsfw() {
		return nsfw;
	}

	public void setNsfw(Boolean nsfw) {
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

}
