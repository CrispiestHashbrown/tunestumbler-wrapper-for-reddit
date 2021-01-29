package ca.tunestumbler.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.service.SubredditService;
import ca.tunestumbler.api.service.impl.helpers.SubredditHelpers;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditDataChildrenModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditFetchResponseModel;

@Service
public class SubredditServiceImpl implements SubredditService {

	@Autowired
	SubredditHelpers subredditHelpers;
	
	@Autowired
	SharedUtils sharedUtils;

//	TODO: Get subreddits from paginated results
	@Override
	public List<SubredditDTO> fetchSubreddits(UserDTO user) {
		SubredditFetchResponseModel response = subredditHelpers.sendGetSubredditRequest(user);
		List<SubredditDataChildrenModel> subredditModel = response.getData().getChildren();
		if (subredditModel == null || subredditModel.isEmpty()) {
			return new ArrayList<>();
		}

		List<SubredditDTO> subredditDTOs = new ArrayList<>();
		for (SubredditDataChildrenModel data : subredditModel) {
			String subreddit = data.getData().getDisplay_name();
			SubredditDTO subredditDTO = subredditHelpers.createNewSubredditDTO(subreddit, response);
			subredditDTOs.add(subredditDTO);
		}

		return subredditDTOs;
	}

}
