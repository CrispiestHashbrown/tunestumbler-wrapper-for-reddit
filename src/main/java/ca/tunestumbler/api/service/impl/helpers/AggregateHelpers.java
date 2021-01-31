package ca.tunestumbler.api.service.impl.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.AggregateDTO;
import ca.tunestumbler.api.shared.dto.MultiredditDTO;
import ca.tunestumbler.api.shared.dto.SubredditDTO;

@Component
public class AggregateHelpers {

	@Autowired
	SharedUtils sharedUtils;

	public AggregateDTO addSubreddit(String userId, SubredditDTO subredditDTO) {
		AggregateDTO newAggregateDTO = new AggregateDTO();
		int idLength = 50;
		String aggregateId = sharedUtils.generateAggregateId(idLength);

		newAggregateDTO.setAggregateId(aggregateId);
		newAggregateDTO.setUserId(userId);
		newAggregateDTO.setSubredditId(subredditDTO.getSubredditId());
		newAggregateDTO.setMultireddit("");
		newAggregateDTO.setSubreddit(subredditDTO.getSubreddit());

		return newAggregateDTO;
	}

	public AggregateDTO addMultireddit(String userId, MultiredditDTO multiredditDTO) {
		AggregateDTO newAggregateDTO = new AggregateDTO();
		int idLength = 50;
		String aggregateId = sharedUtils.generateAggregateId(idLength);

		newAggregateDTO.setAggregateId(aggregateId);
		newAggregateDTO.setUserId(userId);
		newAggregateDTO.setMultiredditId(multiredditDTO.getMultiredditId());
		newAggregateDTO.setMultireddit(multiredditDTO.getMultireddit());
		newAggregateDTO.setSubreddit(multiredditDTO.getSubreddit());

		return newAggregateDTO;
	}

}
