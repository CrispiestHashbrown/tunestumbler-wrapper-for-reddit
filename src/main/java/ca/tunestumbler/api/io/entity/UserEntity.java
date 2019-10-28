package ca.tunestumbler.api.io.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "users")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = -4547528289363043198L;

	@Id
	@Column(nullable = false, unique = true)
	private String userId;

	@Column(nullable = false, length = 120, unique = true)
	private String email;

	@Column(nullable = false)
	private String encryptedPassword;

	private String emailVerificationToken;

	@Column(nullable = false)
	private Boolean emailVerificationStatus = false;

	@Column(nullable = true, length = 20)
	private String redditAccountName;

	@Column(nullable = true, unique = true)
	private String token;

	@Column(nullable = true, unique = true)
	private String refreshToken;

	@Column(nullable = true)
	private String tokenLifetime;

	@Column(nullable = false)
	private String lastModified;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity")
	private List<AuthValidationEntity> validation;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity")
	private List<SubredditEntity> subreddit;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity")
	private List<MultiredditEntity> multireddit;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity")
	private List<FiltersEntity> filters;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity")
	private List<AggregateEntity> aggregate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getEmailVerificationToken() {
		return emailVerificationToken;
	}

	public void setEmailVerificationToken(String emailVerificationToken) {
		this.emailVerificationToken = emailVerificationToken;
	}

	public Boolean getEmailVerificationStatus() {
		return emailVerificationStatus;
	}

	public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
		this.emailVerificationStatus = emailVerificationStatus;
	}

	public String getRedditAccountName() {
		return redditAccountName;
	}

	public void setRedditAccountName(String redditAccountName) {
		this.redditAccountName = redditAccountName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenLifetime() {
		return tokenLifetime;
	}

	public void setTokenLifetime(String tokenLifetime) {
		this.tokenLifetime = tokenLifetime;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public List<AuthValidationEntity> getValidation() {
		return validation;
	}

	public void setValidation(List<AuthValidationEntity> validation) {
		this.validation = validation;
	}

	public List<SubredditEntity> getSubreddit() {
		return subreddit;
	}

	public void setSubreddit(List<SubredditEntity> subreddit) {
		this.subreddit = subreddit;
	}

	public List<MultiredditEntity> getMultireddit() {
		return multireddit;
	}

	public void setMultireddit(List<MultiredditEntity> multireddit) {
		this.multireddit = multireddit;
	}

	public List<FiltersEntity> getFilters() {
		return filters;
	}

	public void setFilters(List<FiltersEntity> filters) {
		this.filters = filters;
	}

	public List<AggregateEntity> getAggregate() {
		return aggregate;
	}

	public void setAggregate(List<AggregateEntity> aggregate) {
		this.aggregate = aggregate;
	}

}
