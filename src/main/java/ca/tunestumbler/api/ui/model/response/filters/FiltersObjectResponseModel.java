package ca.tunestumbler.api.ui.model.response.filters;

public class FiltersObjectResponseModel {
	private String filtersId;
	private String userId;
	private String multireddit;
	private String subreddit;
	private Integer priority;
	private Integer minScore;
	private Boolean allowNSFWFlag = false;
	private String hideByKeyword;
	private String showByKeyword;
	private String hideByDomain;
	private String showByDomain;

	public String getFiltersId() {
		return filtersId;
	}

	public void setFiltersId(String filtersId) {
		this.filtersId = filtersId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getMinScore() {
		return minScore;
	}

	public void setMinScore(Integer minScore) {
		this.minScore = minScore;
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
