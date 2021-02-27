package ca.tunestumbler.api.ui.model.request.filter.update.object;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class UpdateFiltersObjectRequestModel {
	@NotBlank
	@NotNull
	@NotEmpty
	private String filtersId;

	@NotBlank
	@NotNull
	@NotEmpty
	private String subreddit;

	@Positive
	@NotNull
	private Integer minScore;

	@NotNull
	private Boolean allowNSFWFlag;

	@NotNull
	@Valid
	private List<UpdateExcludedDomainObjectRequestModel> excludedDomains;

	@NotNull
	@Valid
	private List<UpdateExcludedKeywordObjectRequestModel> excludedKeywords;

	@NotNull
	@Valid
	private List<UpdateSelectedDomainObjectRequestModel> selectedDomains;

	@NotNull
	@Valid
	private List<UpdateSelectedKeywordObjectRequestModel> selectedKeywords;

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

	public List<UpdateExcludedDomainObjectRequestModel> getExcludedDomains() {
		return excludedDomains;
	}

	public void setExcludedDomains(List<UpdateExcludedDomainObjectRequestModel> excludedDomains) {
		this.excludedDomains = excludedDomains;
	}

	public List<UpdateExcludedKeywordObjectRequestModel> getExcludedKeywords() {
		return excludedKeywords;
	}

	public void setExcludedKeywords(List<UpdateExcludedKeywordObjectRequestModel> excludedKeywords) {
		this.excludedKeywords = excludedKeywords;
	}

	public List<UpdateSelectedDomainObjectRequestModel> getSelectedDomains() {
		return selectedDomains;
	}

	public void setSelectedDomains(List<UpdateSelectedDomainObjectRequestModel> selectedDomains) {
		this.selectedDomains = selectedDomains;
	}

	public List<UpdateSelectedKeywordObjectRequestModel> getSelectedKeywords() {
		return selectedKeywords;
	}

	public void setSelectedKeywords(List<UpdateSelectedKeywordObjectRequestModel> selectedKeywords) {
		this.selectedKeywords = selectedKeywords;
	}

}
