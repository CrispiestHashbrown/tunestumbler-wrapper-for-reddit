package ca.tunestumbler.api.ui.model.response.results;

import java.util.List;

public class ResultsDataModel {
	private List<ResultsDataChildrenModel> children;
	private String after;
	private String before;

	public List<ResultsDataChildrenModel> getChildren() {
		return children;
	}

	public void setChildren(List<ResultsDataChildrenModel> children) {
		this.children = children;
	}

	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}

	public String getBefore() {
		return before;
	}

	public void setBefore(String before) {
		this.before = before;
	}

}
