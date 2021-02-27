package ca.tunestumbler.api.ui.model.request.filter.update.object;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateExcludedKeywordObjectRequestModel {
	private String excludedKeywordId;

	@NotBlank
	@NotNull
	@NotEmpty
	private String excludedKeyword;

	public String getExcludedKeywordId() {
		return excludedKeywordId;
	}

	public void setExcludedKeywordId(String excludedKeywordId) {
		this.excludedKeywordId = excludedKeywordId;
	}

	public String getExcludedKeyword() {
		return excludedKeyword;
	}

	public void setExcludedKeyword(String excludedKeyword) {
		this.excludedKeyword = excludedKeyword;
	}

}
