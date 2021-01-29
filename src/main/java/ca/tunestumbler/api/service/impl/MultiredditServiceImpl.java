package ca.tunestumbler.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.service.MultiredditService;
import ca.tunestumbler.api.service.impl.helpers.MultiredditHelpers;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.MultiredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditFetchResponseModel;
import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditDataSubredditModel;

@Service
public class MultiredditServiceImpl implements MultiredditService {

	@Autowired
	MultiredditHelpers multiredditHelpers;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public List<MultiredditDTO> fetchMultireddits(UserDTO user) {
		MultiredditFetchResponseModel[] response = multiredditHelpers.sendGetMultiredditRequest(user);
		List<MultiredditDTO> multireddits = new ArrayList<>();
		List<MultiredditFetchResponseModel> multiredditModel = Arrays.asList(response);
		if (multiredditModel.isEmpty()) {
			return multireddits;
		}

		for (MultiredditFetchResponseModel multireddit : multiredditModel) {
			for (MultiredditDataSubredditModel subreddit : multireddit.getData().getSubreddits()) {
				MultiredditDTO multiredditDTO = multiredditHelpers.createNewMultiredditDTO(multireddit, subreddit);
				multireddits.add(multiredditDTO);
			}
		}

		return multireddits;
	}

}
