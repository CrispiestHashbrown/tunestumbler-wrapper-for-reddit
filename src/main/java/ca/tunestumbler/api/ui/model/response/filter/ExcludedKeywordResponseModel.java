package ca.tunestumbler.api.ui.model.response.filter;

import java.util.List;

import ca.tunestumbler.api.ui.model.response.filter.object.ExcludedKeywordObjectResponseModel;

public class ExcludedKeywordResponseModel {
	List<ExcludedKeywordObjectResponseModel> excludedKeywords;

	public List<ExcludedKeywordObjectResponseModel> getExcludedKeywords() {
		return excludedKeywords;
	}

	public void setExcludedKeywords(List<ExcludedKeywordObjectResponseModel> excludedKeywords) {
		this.excludedKeywords = excludedKeywords;
	}

}
