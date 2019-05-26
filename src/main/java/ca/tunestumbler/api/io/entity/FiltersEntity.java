package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "filter")
public class FiltersEntity implements Serializable {
	private static final long serialVersionUID = 661201748897792032L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	private String filtersId;

	@ManyToOne
	@JoinColumn(name = "users_id")
	private UserEntity userEntity;

	@Column(length = 50)
	private String multireddit;

	@Column(length = 21)
	private String subreddit;

	@Column(nullable = false)
	private Boolean allowNSFWFlag = false;

	@Column(length = 50)
	private String hideByKeyword;

	@Column(length = 50)
	private String showByKeyword;

	@Column(length = 15)
	private String hideByDomain;

	@Column(length = 15)
	private String showByDomain;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFiltersId() {
		return filtersId;
	}

	public void setFiltersId(String filtersId) {
		this.filtersId = filtersId;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
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

	public Boolean getAllowNSFWFlag() {
		return allowNSFWFlag;
	}

	public void setAllowNSFWFlag(Boolean allowNSFWFlag) {
		this.allowNSFWFlag = allowNSFWFlag;
	}

	public String getHideByKeyword() {
		return hideByKeyword;
	}

	public void setHideByKeyword(String hideByKeyword) {
		this.hideByKeyword = hideByKeyword;
	}

	public String getShowByKeyword() {
		return showByKeyword;
	}

	public void setShowByKeyword(String showByKeyword) {
		this.showByKeyword = showByKeyword;
	}

	public String getHideByDomain() {
		return hideByDomain;
	}

	public void setHideByDomain(String hideByDomain) {
		this.hideByDomain = hideByDomain;
	}

	public String getShowByDomain() {
		return showByDomain;
	}

	public void setShowByDomain(String showByDomain) {
		this.showByDomain = showByDomain;
	}

}
