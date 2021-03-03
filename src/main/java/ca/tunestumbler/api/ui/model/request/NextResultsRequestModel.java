package ca.tunestumbler.api.ui.model.request;

import javax.validation.constraints.NotEmpty;

public class NextResultsRequestModel {

	@NotEmpty(message = "Next uri cannot be empty")
	private String nextUri;

	@NotEmpty(message = "AfterId cannot be empty")
	private String afterId;

	public String getNextUri() {
		return nextUri;
	}

	public void setNextUri(String nextUri) {
		this.nextUri = nextUri;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

}
