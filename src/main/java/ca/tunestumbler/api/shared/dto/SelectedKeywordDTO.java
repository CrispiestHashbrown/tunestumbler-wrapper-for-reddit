package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class SelectedKeywordDTO implements Serializable {
	private static final long serialVersionUID = 6283128632928920343L;
	private String selectedKeywordId;
	private String filtersId;
	private String selectedKeyword;

	public String getSelectedKeywordId() {
		return selectedKeywordId;
	}

	public void setSelectedKeywordId(String selectedKeywordId) {
		this.selectedKeywordId = selectedKeywordId;
	}

	public String getFiltersId() {
		return filtersId;
	}

	public void setFiltersId(String filtersId) {
		this.filtersId = filtersId;
	}

	public String getSelectedKeyword() {
		return selectedKeyword;
	}

	public void setSelectedKeyword(String selectedKeyword) {
		this.selectedKeyword = selectedKeyword;
	}

}
