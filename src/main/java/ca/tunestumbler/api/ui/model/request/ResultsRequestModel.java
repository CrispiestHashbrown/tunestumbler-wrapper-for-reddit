package ca.tunestumbler.api.ui.model.request;

public class ResultsRequestModel {
	private String nextUri;
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
