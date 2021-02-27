package ca.tunestumbler.api.ui.model.response.filter.object;

import java.util.List;

public class FiltersObjectResponseModel {
	private String filtersId;
	private String subreddit;
	private Integer minScore;
	private Boolean allowNSFWFlag;
	private List<ExcludedDomainObjectResponseModel> excludedDomains;
	private List<ExcludedKeywordObjectResponseModel> excludedKeywords;
	private List<SelectedDomainObjectResponseModel> selectedDomains;
	private List<SelectedKeywordObjectResponseModel> selectedKeywords;

	public String getFiltersId() {
		return filtersId;
	}

	public void setFiltersId(String filtersId) {
		this.filtersId = filtersId;
	}

	public String getSubreddit() {
		return subreddit;
	}

	public void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
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

	public List<ExcludedDomainObjectResponseModel> getExcludedDomains() {
		return excludedDomains;
	}

	public void setExcludedDomains(List<ExcludedDomainObjectResponseModel> excludedDomains) {
		this.excludedDomains = excludedDomains;
	}

	public List<ExcludedKeywordObjectResponseModel> getExcludedKeywords() {
		return excludedKeywords;
	}

	public void setExcludedKeywords(List<ExcludedKeywordObjectResponseModel> excludedKeywords) {
		this.excludedKeywords = excludedKeywords;
	}

	public List<SelectedDomainObjectResponseModel> getSelectedDomains() {
		return selectedDomains;
	}

	public void setSelectedDomains(List<SelectedDomainObjectResponseModel> selectedDomains) {
		this.selectedDomains = selectedDomains;
	}

	public List<SelectedKeywordObjectResponseModel> getSelectedKeywords() {
		return selectedKeywords;
	}

	public void setSelectedKeywords(List<SelectedKeywordObjectResponseModel> selectedKeywords) {
		this.selectedKeywords = selectedKeywords;
	}

}