package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;
import java.util.List;

public class UserDTO implements Serializable {
	private static final long serialVersionUID = 9002611090405917353L;
	private String userId;
	private String email;
	private String password;
	private String encryptedPassword;
	private String emailVerificationToken;
	private Boolean emailVerificationStatus = false;
	private String redditAccountName;
	private String token;
	private String refreshToken;
	private String tokenLifetime;
	private String lastModified;
	private List<AuthValidationDTO> validation;
	private List<SubredditDTO> subreddit;
	private List<MultiredditDTO> multireddit;
	private List<FiltersDTO> filters;
	private List<AggregateDTO> aggregate;
	private List<ResultsDTO> results;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public List<AuthValidationDTO> getValidation() {
		return validation;
	}

	public void setValidation(List<AuthValidationDTO> validation) {
		this.validation = validation;
	}

	public List<SubredditDTO> getSubreddit() {
		return subreddit;
	}

	public void setSubreddit(List<SubredditDTO> subreddit) {
		this.subreddit = subreddit;
	}

	public List<MultiredditDTO> getMultireddit() {
		return multireddit;
	}

	public void setMultireddit(List<MultiredditDTO> multireddit) {
		this.multireddit = multireddit;
	}

	public List<FiltersDTO> getFilters() {
		return filters;
	}

	public void setFilters(List<FiltersDTO> filters) {
		this.filters = filters;
	}

	public List<AggregateDTO> getAggregate() {
		return aggregate;
	}

	public void setAggregate(List<AggregateDTO> aggregate) {
		this.aggregate = aggregate;
	}

	public List<ResultsDTO> getResults() {
		return results;
	}

	public void setResults(List<ResultsDTO> results) {
		this.results = results;
	}

}
