package ca.tunestumbler.api.shared.dto;

public class NextResultsRequestDTO {
	private String afterId;
	private String nextUri;

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public String getNextUri() {
		return nextUri;
	}

	public void setNextUri(String nextUri) {
		this.nextUri = nextUri;
	}

}
