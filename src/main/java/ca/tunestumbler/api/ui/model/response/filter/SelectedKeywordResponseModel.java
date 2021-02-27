package ca.tunestumbler.api.ui.model.response.filter;

import java.util.List;

import ca.tunestumbler.api.ui.model.response.filter.object.SelectedKeywordObjectResponseModel;

public class SelectedKeywordResponseModel {
	List<SelectedKeywordObjectResponseModel> selectedKeywords;

	public List<SelectedKeywordObjectResponseModel> getSelectedKeywords() {
		return selectedKeywords;
	}

	public void setSelectedKeywords(List<SelectedKeywordObjectResponseModel> selectedKeywords) {
		this.selectedKeywords = selectedKeywords;
	}

}
