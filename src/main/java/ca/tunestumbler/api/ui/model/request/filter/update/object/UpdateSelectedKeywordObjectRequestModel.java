package ca.tunestumbler.api.ui.model.request.filter.update.object;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateSelectedKeywordObjectRequestModel {
	private String selectedKeywordId;

	@NotBlank
	@NotNull
	@NotEmpty
	private String selectedKeyword;

	public String getSelectedKeywordId() {
		return selectedKeywordId;
	}

	public void setSelectedKeywordId(String selectedKeywordId) {
		this.selectedKeywordId = selectedKeywordId;
	}

	public String getSelectedKeyword() {
		return selectedKeyword;
	}

	public void setSelectedKeyword(String selectedKeyword) {
		this.selectedKeyword = selectedKeyword;
	}

}
