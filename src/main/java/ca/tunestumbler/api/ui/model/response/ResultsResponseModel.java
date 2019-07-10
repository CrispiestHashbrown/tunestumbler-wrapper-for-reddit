package ca.tunestumbler.api.ui.model.response;

import java.util.List;

import ca.tunestumbler.api.ui.model.response.results.ResultsObjectResponseModel;

public class ResultsResponseModel {
	private List<ResultsObjectResponseModel> results;

	public List<ResultsObjectResponseModel> getResults() {
		return results;
	}

	public void setResults(List<ResultsObjectResponseModel> results) {
		this.results = results;
	}

}
