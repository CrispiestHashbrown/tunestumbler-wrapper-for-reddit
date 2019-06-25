package ca.tunestumbler.api.ui.model.response.multireddit;

import java.util.List;

public class MultiredditListDataModel {
	private List<MultiredditListDataSubredditModel> subreddits;

	public List<MultiredditListDataSubredditModel> getSubreddits() {
		return subreddits;
	}

	public void setSubreddits(List<MultiredditListDataSubredditModel> subreddits) {
		this.subreddits = subreddits;
	}

}
