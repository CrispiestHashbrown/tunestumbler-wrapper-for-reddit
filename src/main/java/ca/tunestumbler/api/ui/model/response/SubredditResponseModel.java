package ca.tunestumbler.api.ui.model.response;

import java.util.List;

import ca.tunestumbler.api.ui.model.response.subreddit.SubredditObjectResponseModel;

public class SubredditResponseModel {
	private List<SubredditObjectResponseModel> subreddits;

	public List<SubredditObjectResponseModel> getSubreddits() {
		return subreddits;
	}

	public void setSubreddits(List<SubredditObjectResponseModel> subreddits) {
		this.subreddits = subreddits;
	}

}
