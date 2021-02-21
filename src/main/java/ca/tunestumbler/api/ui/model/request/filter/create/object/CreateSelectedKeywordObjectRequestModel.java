package ca.tunestumbler.api.ui.model.request.filter.create.object;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateSelectedKeywordObjectRequestModel {
	@NotBlank
	@NotNull
	@NotEmpty
	private String selectedKeyword;

	public String getSelectedKeyword() {
		return selectedKeyword;
	}

	public void setSelectedKeyword(String selectedKeyword) {
		this.selectedKeyword = selectedKeyword;
	}

}
