package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;
import java.util.List;

public class FiltersDTO implements Serializable {
	private static final long serialVersionUID = -5370336446817047806L;
	private String filtersId;
	private String subreddit;
	private Integer minScore = 1;
	private Boolean allowNSFWFlag = false;
	private List<ExcludedDomainDTO> excludedDomains;
	private List<ExcludedKeywordDTO> excludedKeywords;
	private List<SelectedDomainDTO> selectedDomains;
	private List<SelectedKeywordDTO> selectedKeywords;

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

	public List<ExcludedDomainDTO> getExcludedDomains() {
		return excludedDomains;
	}

	public void setExcludedDomains(List<ExcludedDomainDTO> excludedDomains) {
		this.excludedDomains = excludedDomains;
	}

	public List<ExcludedKeywordDTO> getExcludedKeywords() {
		return excludedKeywords;
	}

	public void setExcludedKeywords(List<ExcludedKeywordDTO> excludedKeywords) {
		this.excludedKeywords = excludedKeywords;
	}

	public List<SelectedDomainDTO> getSelectedDomains() {
		return selectedDomains;
	}

	public void setSelectedDomains(List<SelectedDomainDTO> selectedDomains) {
		this.selectedDomains = selectedDomains;
	}

	public List<SelectedKeywordDTO> getSelectedKeywords() {
		return selectedKeywords;
	}

	public void setSelectedKeywords(List<SelectedKeywordDTO> selectedKeywords) {
		this.selectedKeywords = selectedKeywords;
	}

}
