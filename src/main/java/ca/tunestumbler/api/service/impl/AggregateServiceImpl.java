package ca.tunestumbler.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.io.entity.AggregateEntity;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.AggregateRepository;
import ca.tunestumbler.api.service.AggregateService;
import ca.tunestumbler.api.service.MultiredditService;
import ca.tunestumbler.api.service.SubredditService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.AggregateDTO;
import ca.tunestumbler.api.shared.dto.MultiredditDTO;
import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

@Service
public class AggregateServiceImpl implements AggregateService {

	@Autowired
	AggregateRepository aggregateRepository;

	@Autowired
	UserService userService;

	@Autowired
	SubredditService subredditService;

	@Autowired
	MultiredditService multiredditService;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public List<AggregateDTO> createAggregateByUserId(UserDTO user) {
		String userId = user.getUserId();
		Long userMaxId = aggregateRepository.getMaxIdByUserId(userId);
		Long maxId = aggregateRepository.getMaxId();
		Long startId = sharedUtils.setStartId(userMaxId, maxId);

		List<SubredditDTO> subredditList = subredditService.getSubredditsByUserId(userId);
		List<MultiredditDTO> multiredditList = multiredditService.getMultiredditsByUserId(userId);
		List<AggregateDTO> aggregate = new ArrayList<>();

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		int idLength = 50;
		Boolean isAdded = false;
		for (SubredditDTO subreddit : subredditList) {
			AggregateEntity aggregateEntity = new AggregateEntity();
			String aggregateId = sharedUtils.generateAggregateId(idLength);

			aggregateEntity.setAggregateId(aggregateId);
			aggregateEntity.setUserEntity(userEntity);
			aggregateEntity.setUserId(userId);
			aggregateEntity.setSubredditId(subreddit.getSubredditId());
			aggregateEntity.setSubreddit(subreddit.getSubreddit());
			aggregateEntity.setIsSubredditAdded(isAdded);
			aggregateEntity.setStartId(startId);
			aggregateEntity.setLastModified(sharedUtils.getCurrentTime());

			AggregateEntity storedAggregate = aggregateRepository.save(aggregateEntity);

			AggregateDTO aggregateDTO = new AggregateDTO();
			BeanUtils.copyProperties(storedAggregate, aggregateDTO);
			aggregate.add(aggregateDTO);
		}

		for (MultiredditDTO multireddit : multiredditList) {
			AggregateEntity aggregateEntity = new AggregateEntity();
			String aggregateId = sharedUtils.generateAggregateId(idLength);

			aggregateEntity.setAggregateId(aggregateId);
			aggregateEntity.setUserEntity(userEntity);
			aggregateEntity.setUserId(userId);
			aggregateEntity.setSubredditId(multireddit.getSubreddit());
			aggregateEntity.setSubreddit(multireddit.getSubreddit());
			aggregateEntity.setIsSubredditAdded(isAdded);
			aggregateEntity.setMultiredditId(multireddit.getMultiredditId());
			aggregateEntity.setMultireddit(multireddit.getMultireddit());
			aggregateEntity.setStartId(startId);
			aggregateEntity.setLastModified(sharedUtils.getCurrentTime());

			AggregateEntity storedAggregate = aggregateRepository.save(aggregateEntity);

			AggregateDTO aggregateDTO = new AggregateDTO();
			BeanUtils.copyProperties(storedAggregate, aggregateDTO);
			aggregate.add(aggregateDTO);
		}

		return aggregate;
	}

	@Override
	public List<AggregateDTO> getAggregateByUserId(String userId) {
		List<AggregateDTO> existingAggregate = new ArrayList<>();

		Long startId = aggregateRepository.getMaxIdByUserId(userId);
		List<AggregateEntity> aggregateList = aggregateRepository.findAggregateByUserIdAndMaxId(userId, startId);

		for (AggregateEntity aggregate : aggregateList) {
			AggregateDTO aggregateDTO = new AggregateDTO();
			BeanUtils.copyProperties(aggregate, aggregateDTO);
			existingAggregate.add(aggregateDTO);
		}

		return existingAggregate;
	}

}
