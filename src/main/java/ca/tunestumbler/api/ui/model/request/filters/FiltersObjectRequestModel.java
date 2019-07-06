package ca.tunestumbler.api.ui.model.request.filters;

public class FiltersObjectRequestModel {
	private long id;
	private String filtersId;
	private String multireddit;
	private String subreddit;
	private Integer priority = 0;
	private Boolean allowNSFWFlag = false;
	private String hideByKeyword;
	private String showByKeyword;
	private String hideByDomain;
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
