package ca.tunestumbler.api.ui.model.request.filter.create.object;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateExcludedKeywordObjectRequestModel {
	@NotBlank
	@NotNull
	@NotEmpty
	private String excludedKeyword;

	public String getExcludedKeyword() {
		return excludedKeyword;
	}

	public void setExcludedKeyword(String excludedKeyword) {
		this.excludedKeyword = excludedKeyword;
	}

}
