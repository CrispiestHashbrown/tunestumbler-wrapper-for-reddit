package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class ExcludedKeywordDTO implements Serializable {
	private static final long serialVersionUID = 1745995570027560038L;
	private String excludedKeywordId;
	private String filtersId;
	private String excludedKeyword;

	public String getExcludedKeywordId() {
		return excludedKeywordId;
	}

	public void setExcludedKeywordId(String excludedKeywordId) {
		this.excludedKeywordId = excludedKeywordId;
	}

	public String getFiltersId() {
		return filtersId;
	}

	public void setFiltersId(String filtersId) {
		this.filtersId = filtersId;
	}

	public String getExcludedKeyword() {
		return excludedKeyword;
	}

	public void setExcludedKeyword(String excludedKeyword) {
		this.excludedKeyword = excludedKeyword;
	}

}
