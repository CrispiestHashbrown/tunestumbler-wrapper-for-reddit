package ca.tunestumbler.api.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
		Long userMaxId = aggregateRepository.findMaxIdByUserId(userId);
		Long maxId = aggregateRepository.findMaxId();
		Long startId = sharedUtils.setStartId(userMaxId, maxId);

		List<SubredditDTO> subredditList = subredditService.fetchSubreddits(user);
		List<MultiredditDTO> multiredditList = multiredditService.fetchMultireddits(user);
		List<AggregateDTO> aggregate = new ArrayList<>();

		if (subredditList == null && multiredditList == null) {
			return aggregate;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		List<AggregateEntity> newAggregateEntities = new ArrayList<>();
		for (SubredditDTO subredditDTO : subredditList) {
			AggregateEntity newAggregateEntity = addSubredditEntity(userEntity, subredditDTO, startId);
			newAggregateEntities.add(newAggregateEntity);
		}

		for (MultiredditDTO multiredditDTO : multiredditList) {
			AggregateEntity newAggregateEntity = addMultiredditEntity(userEntity, multiredditDTO, startId);
			newAggregateEntities.add(newAggregateEntity);
		}

		List<AggregateEntity> storedAggregateEntities = aggregateRepository.saveAll(newAggregateEntities);
		Type listType = new TypeToken<List<AggregateDTO>>() {
		}.getType();
		aggregate = new ModelMapper().map(storedAggregateEntities, listType);

		return aggregate;
	}

	@Override
	public List<AggregateDTO> getAggregateByUserId(String userId) {
		List<AggregateDTO> existingAggregate = new ArrayList<>();

		Long startId = aggregateRepository.findMaxStartIdByUserId(userId);
		List<AggregateEntity> aggregateList = aggregateRepository
				.findAggregateByUserIdAndMaxStartIdAndIsSubredditAdded(userId, startId);

		for (AggregateEntity aggregate : aggregateList) {
			AggregateDTO aggregateDTO = new AggregateDTO();
			BeanUtils.copyProperties(aggregate, aggregateDTO);
			existingAggregate.add(aggregateDTO);
		}

		return existingAggregate;
	}

	@Override
	public List<AggregateDTO> updateAggregateByUserId(UserDTO user) {
		List<AggregateDTO> updatedAggregate = new ArrayList<>();

		String userId = user.getUserId();
		Long startId = aggregateRepository.findMaxStartIdByUserId(userId);
		List<AggregateEntity> aggregateEntities = aggregateRepository
				.findAggregateByUserIdAndMaxStartIdAndIsSubredditAdded(userId, startId);
		for (AggregateEntity aggregateEntity : aggregateEntities) {
			aggregateEntity.setIsSubredditAdded(false);
		}

		List<SubredditDTO> subredditList = subredditService.updateSubreddits(user);
		List<MultiredditDTO> multiredditList = multiredditService.updateMultireddits(user);
		if (subredditList == null && multiredditList == null) {
			aggregateRepository.saveAll(aggregateEntities);
			return updatedAggregate;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		Boolean isSubredditAdded = true;
		List<AggregateEntity> updatedAggregateEntities = new ArrayList<>();
		for (SubredditDTO subredditDTO : subredditList) {
			String subreddit = subredditDTO.getSubreddit();
			List<AggregateEntity> aggregateEntity = aggregateRepository.findByUserIdAndSubredditAndMaxStartId(userId,
					subreddit, startId);

			if (!aggregateEntity.isEmpty()) {
				for (AggregateEntity aggregateToUpdate : aggregateEntity) {
					aggregateToUpdate.setIsSubredditAdded(isSubredditAdded);
					aggregateToUpdate.setLastModified(sharedUtils.getCurrentTime());
					updatedAggregateEntities.add(aggregateToUpdate);
				}
			} else {
				AggregateEntity newAggregateEntity = addSubredditEntity(userEntity, subredditDTO, startId);
				updatedAggregateEntities.add(newAggregateEntity);
			}
		}

		for (MultiredditDTO multiredditDTO : multiredditList) {
			String multireddit = multiredditDTO.getMultireddit();
			String subreddit = multiredditDTO.getSubreddit();
			List<AggregateEntity> aggregateEntity = aggregateRepository
					.findByUserIdAndMultiredditAndSubredditAndMaxStartId(userId, multireddit, subreddit, startId);

			if (!aggregateEntity.isEmpty()) {
				for (AggregateEntity aggregateToUpdate : aggregateEntity) {
					aggregateToUpdate.setIsSubredditAdded(isSubredditAdded);
					aggregateToUpdate.setLastModified(sharedUtils.getCurrentTime());
					updatedAggregateEntities.add(aggregateToUpdate);
				}
			} else {
				AggregateEntity newAggregateEntity = addMultiredditEntity(userEntity, multiredditDTO, startId);
				updatedAggregateEntities.add(newAggregateEntity);
			}
		}

		List<AggregateEntity> storedAggregateEntities = aggregateRepository.saveAll(updatedAggregateEntities);
		Type listType = new TypeToken<List<AggregateDTO>>() {
		}.getType();
		updatedAggregate = new ModelMapper().map(storedAggregateEntities, listType);

		return updatedAggregate;
	}

	private AggregateEntity addSubredditEntity(UserEntity userEntity, SubredditDTO subredditDTO, Long startId) {
		AggregateEntity newAggregateEntity = new AggregateEntity();
		int idLength = 50;
		String aggregateId = sharedUtils.generateAggregateId(idLength);
		Boolean isSubredditAdded = true;

		newAggregateEntity.setAggregateId(aggregateId);
		newAggregateEntity.setUserEntity(userEntity);
		newAggregateEntity.setUserId(userEntity.getUserId());
		newAggregateEntity.setSubredditId(subredditDTO.getSubredditId());
		newAggregateEntity.setSubreddit(subredditDTO.getSubreddit());
		newAggregateEntity.setIsSubredditAdded(isSubredditAdded);
		newAggregateEntity.setStartId(startId);
		newAggregateEntity.setLastModified(sharedUtils.getCurrentTime());

		return newAggregateEntity;
	}

	private AggregateEntity addMultiredditEntity(UserEntity userEntity, MultiredditDTO multiredditDTO, Long startId) {
		AggregateEntity newAggregateEntity = new AggregateEntity();
		int idLength = 50;
		String aggregateId = sharedUtils.generateAggregateId(idLength);
		Boolean isSubredditAdded = true;

		newAggregateEntity.setAggregateId(aggregateId);
		newAggregateEntity.setUserEntity(userEntity);
		newAggregateEntity.setUserId(userEntity.getUserId());
		newAggregateEntity.setMultiredditId(multiredditDTO.getMultiredditId());
		newAggregateEntity.setMultireddit(multiredditDTO.getMultireddit());
		newAggregateEntity.setSubreddit(multiredditDTO.getSubreddit());
		newAggregateEntity.setIsSubredditAdded(isSubredditAdded);
		newAggregateEntity.setStartId(startId);
		newAggregateEntity.setLastModified(sharedUtils.getCurrentTime());

		return newAggregateEntity;
	}

}
