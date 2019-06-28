package ca.tunestumbler.api.ui.model.response.multireddit;

import java.util.List;

public class MultiredditDataModel {
	private String name;
	private List<MultiredditDataSubredditModel> subreddits;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MultiredditDataSubredditModel> getSubreddits() {
		return subreddits;
	}

	public void setSubreddits(List<MultiredditDataSubredditModel> subreddits) {
		this.subreddits = subreddits;
	}

}
