package ca.tunestumbler.api.ui.model.request.filter.create.object;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreateFiltersObjectRequestModel {
	@NotBlank
	@NotNull
	@NotEmpty
	private String subreddit;

	@Positive
	@NotNull(message = "minScore cannot be null.")
	private Integer minScore;

	@NotNull(message = "allowNSFWFlag cannot be null.")
	private Boolean allowNSFWFlag;

	@NotNull
	@Valid
	private List<CreateExcludedDomainObjectRequestModel> excludedDomains;

	@NotNull
	@Valid
	private List<CreateExcludedKeywordObjectRequestModel> excludedKeywords;

	@NotNull
	@Valid
	private List<CreateSelectedDomainObjectRequestModel> selectedDomains;

	@NotNull
	@Valid
	private List<CreateSelectedKeywordObjectRequestModel> selectedKeywords;

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

	public List<CreateExcludedDomainObjectRequestModel> getExcludedDomains() {
		return excludedDomains;
	}

	public void setExcludedDomains(List<CreateExcludedDomainObjectRequestModel> excludedDomains) {
		this.excludedDomains = excludedDomains;
	}

	public List<CreateExcludedKeywordObjectRequestModel> getExcludedKeywords() {
		return excludedKeywords;
	}

	public void setExcludedKeywords(List<CreateExcludedKeywordObjectRequestModel> excludedKeywords) {
		this.excludedKeywords = excludedKeywords;
	}

	public List<CreateSelectedDomainObjectRequestModel> getSelectedDomains() {
		return selectedDomains;
	}

	public void setSelectedDomains(List<CreateSelectedDomainObjectRequestModel> selectedDomains) {
		this.selectedDomains = selectedDomains;
	}

	public List<CreateSelectedKeywordObjectRequestModel> getSelectedKeywords() {
		return selectedKeywords;
	}

	public void setSelectedKeywords(List<CreateSelectedKeywordObjectRequestModel> selectedKeywords) {
		this.selectedKeywords = selectedKeywords;
	}

}
