package ca.tunestumbler.api.ui.model.response.subreddit;

import java.util.List;

public class SubredditDataModel {
	private List<SubredditDataChildrenModel> children;
	private String after;
	private String before;

	public List<SubredditDataChildrenModel> getChildren() {
		return children;
	}

	public void setChildren(List<SubredditDataChildrenModel> children) {
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
