package ca.tunestumbler.api.ui.model.response;

import java.util.List;

import ca.tunestumbler.api.ui.model.response.results.ResultsObjectResponseModel;

public class ResultsResponseModel {
	private List<ResultsObjectResponseModel> results;
	private String nextUri;
	private String afterId;

	public List<ResultsObjectResponseModel> getResults() {
		return results;
	}

	public void setResults(List<ResultsObjectResponseModel> results) {
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
