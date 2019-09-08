package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "results")
public class ResultsEntity implements Serializable {
	private static final long serialVersionUID = -8505575230126284466L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private long id;

	@Column(nullable = false, unique = true)
	private String resultsId;

	@ManyToOne
	@JoinColumn(name = "users_userId")
	private UserEntity userEntity;

	@Column()
	private String userId;

	@Column(nullable = false, length = 21)
	private String subreddit;

	@Column(nullable = false, length = 300)
	private String title;

	@Column(nullable = false)
	private int score;

	@Column(nullable = false)
	private long created;

	@Column(nullable = false)
	private long createdUtc;

	@Column(nullable = false)
	private String domain;

	@Column(nullable = false)
	private Boolean isNsfw;

	@Column(nullable = false)
	private Boolean isSpoiler;

	@Column(nullable = false)
	private String permalink;

	@Column(nullable = false)
	private Boolean isStickied;

	@Column(nullable = false)
	private String url;

	@Column
	private String nextUri;

	@Column
	private String afterId;

	@Column()
	private Long startId;

	@Column(nullable = false)
	private String lastModified;

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

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
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

	public String getNextUri() {
		return nextUri;
	}

	public void setNextUri(String nextUri) {
		this.nextUri = nextUri;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public Long getStartId() {
		return startId;
	}

	public void setStartId(Long startId) {
		this.startId = startId;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

}
