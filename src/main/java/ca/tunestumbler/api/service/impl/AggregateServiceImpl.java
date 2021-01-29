package ca.tunestumbler.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.exceptions.SubredditsNotFoundException;
import ca.tunestumbler.api.service.AggregateService;
import ca.tunestumbler.api.service.MultiredditService;
import ca.tunestumbler.api.service.SubredditService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.service.impl.helpers.AggregateHelpers;
import ca.tunestumbler.api.shared.dto.AggregateDTO;
import ca.tunestumbler.api.shared.dto.MultiredditDTO;
import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;

@Service
public class AggregateServiceImpl implements AggregateService {

	@Autowired
	UserService userService;

	@Autowired
	SubredditService subredditService;

	@Autowired
	MultiredditService multiredditService;

	@Autowired
	AggregateHelpers aggregateHelpers;
	
	@Override
	public List<AggregateDTO> getAggregateByUserId(UserDTO user) {
		String userId = user.getUserId();

		List<SubredditDTO> subredditList = subredditService.fetchSubreddits(user);
		List<MultiredditDTO> multiredditList = multiredditService.fetchMultireddits(user);
		if ((subredditList == null || subredditList.isEmpty()) && (multiredditList == null || multiredditList.isEmpty())) {
			throw new SubredditsNotFoundException(ErrorPrefixes.AGGREGATE_SERVICE.getErrorPrefix()
					+ ErrorMessages.SUBREDDIT_RESOURCES_NOT_FOUND.getErrorMessage());
		}

		List<AggregateDTO> aggregateList = new ArrayList<>();
		for (SubredditDTO subredditDTO : subredditList) {
			AggregateDTO aggregateDTO = aggregateHelpers.addSubreddit(userId, subredditDTO);
			aggregateList.add(aggregateDTO);
		}

		for (MultiredditDTO multiredditDTO : multiredditList) {
			AggregateDTO aggregateDTO = aggregateHelpers.addMultireddit(userId, multiredditDTO);
			aggregateList.add(aggregateDTO);
		}

		return aggregateList;
	}

}
