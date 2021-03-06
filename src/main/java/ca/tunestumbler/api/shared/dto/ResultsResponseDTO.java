package ca.tunestumbler.api.shared.dto;

import java.util.List;

public class ResultsResponseDTO {
	private List<ResultsDTO> results;
	private String nextUri;
	private String afterId;

	public List<ResultsDTO> getResults() {
		return results;
	}

	public void setResults(List<ResultsDTO> results) {
		this.results = results;
	}

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
