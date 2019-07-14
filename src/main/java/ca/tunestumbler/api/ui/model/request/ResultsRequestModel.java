package ca.tunestumbler.api.ui.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ResultsRequestModel {

	@NotNull(message = "Next uri cannot be null")
	@NotEmpty(message = "Next uri cannot be empty")
	private String nextUri;

	@NotNull(message = "AfterId cannot be null")
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
