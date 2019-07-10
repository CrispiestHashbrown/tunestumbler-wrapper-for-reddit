package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class ResultsRequestDTO implements Serializable {
	private static final long serialVersionUID = -1892075286214457579L;
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
