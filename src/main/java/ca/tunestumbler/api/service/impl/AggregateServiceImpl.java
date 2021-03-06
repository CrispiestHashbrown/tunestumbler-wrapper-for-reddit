package ca.tunestumbler.api.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import ca.tunestumbler.api.exceptions.SubredditsNotFoundException;
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
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;

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
	public List<AggregateDTO> getAggregateByUserId(UserDTO user) {
		String userId = user.getUserId();
		Long startId = aggregateRepository.findMaxStartIdByUserId(userId);
		List<AggregateEntity> aggregateList = aggregateRepository.findByUserIdAndMaxStartId(userId, startId);

		if (aggregateList == null || aggregateList.isEmpty()) {
			return createAggregateByUserId(user);
		} else {
			return updateAggregateByUserId(user);
		}
	}

	@Override
	public List<AggregateDTO> createAggregateByUserId(UserDTO user) {
		String userId = user.getUserId();
		Long userMaxId = aggregateRepository.findMaxIdByUserId(userId);
		Long maxId = aggregateRepository.findMaxId();
		Long startId = sharedUtils.setStartId(userMaxId, maxId);

		List<SubredditDTO> subredditList = subredditService.fetchSubreddits(user);
		List<MultiredditDTO> multiredditList = multiredditService.fetchMultireddits(user);
		if ((subredditList == null || subredditList.isEmpty()) && (multiredditList == null || multiredditList.isEmpty())) {
			throw new SubredditsNotFoundException(ErrorPrefixes.AGGREGATE_SERVICE.getErrorPrefix()
					+ ErrorMessages.SUBREDDIT_RESOURCES_NOT_FOUND.getErrorMessage());
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		List<AggregateEntity> newAggregateEntities = new ArrayList<>();
		for (SubredditDTO subredditDTO : subredditList) {
			AggregateEntity newAggregateEntity = addSubredditEntity(userId, subredditDTO, startId);
			newAggregateEntities.add(newAggregateEntity);
		}

		for (MultiredditDTO multiredditDTO : multiredditList) {
			AggregateEntity newAggregateEntity = addMultiredditEntity(userId, multiredditDTO, startId);
			newAggregateEntities.add(newAggregateEntity);
		}

		List<AggregateEntity> storedAggregateEntities = aggregateRepository.saveAll(newAggregateEntities);
		Type listType = new TypeToken<List<AggregateDTO>>() {
		}.getType();

		return new ModelMapper().map(storedAggregateEntities, listType);
	}

	@Override
	public List<AggregateDTO> updateAggregateByUserId(UserDTO user) {
		String userId = user.getUserId();
		Long startId = aggregateRepository.findMaxStartIdByUserId(userId);
		List<AggregateEntity> aggregateEntities = aggregateRepository
				.findByUserIdAndMaxStartIdAndIsSubredditAdded(userId, startId);
		
		for (AggregateEntity aggregateEntity : aggregateEntities) {
			aggregateEntity.setIsSubredditAdded(false);
		}

		List<SubredditDTO> subredditList = subredditService.updateSubreddits(user);
		List<MultiredditDTO> multiredditList = multiredditService.updateMultireddits(user);
		List<AggregateDTO> updatedAggregate = new ArrayList<>();
		if ((subredditList == null || subredditList.isEmpty()) && (multiredditList == null || multiredditList.isEmpty())) {
			if (aggregateEntities == null || !aggregateEntities.isEmpty()) {
				aggregateRepository.saveAll(aggregateEntities);
			}

			return updatedAggregate;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		List<AggregateEntity> updatedAggregateEntities = createUpdatedAggregateEntity(subredditList, multiredditList,
				userEntity, startId);

		List<AggregateEntity> storedAggregateEntities = aggregateRepository.saveAll(updatedAggregateEntities);
		Type listType = new TypeToken<List<AggregateDTO>>() {
		}.getType();
		updatedAggregate = new ModelMapper().map(storedAggregateEntities, listType);

		return updatedAggregate;
	}

	private List<AggregateEntity> createUpdatedAggregateEntity(List<SubredditDTO> subredditList,
			List<MultiredditDTO> multiredditList, UserEntity userEntity, Long startId) {
		String userId = userEntity.getUserId();
		List<AggregateEntity> updatedAggregateEntities = new ArrayList<>();

		List<AggregateEntity> aggregateEntities = aggregateRepository.findByUserIdAndMaxStartId(userId, startId);
		Table<String, String, AggregateEntity> aggregateMap = HashBasedTable.create();
		for (AggregateEntity aggregateEntity : aggregateEntities) {
			aggregateMap.put(aggregateEntity.getMultireddit(), aggregateEntity.getSubreddit(), aggregateEntity);
		}

		for (SubredditDTO subredditDTO : subredditList) {
			String multireddit = "";
			AggregateEntity aggregateToUpdate = aggregateMap.get(multireddit, subredditDTO.getSubreddit());
			if (aggregateToUpdate != null) {
				aggregateToUpdate.setIsSubredditAdded(true);
				aggregateToUpdate.setLastModified(sharedUtils.getCurrentTime());
				updatedAggregateEntities.add(aggregateToUpdate);
			} else {
				AggregateEntity newAggregateEntity = addSubredditEntity(userId, subredditDTO, startId);
				updatedAggregateEntities.add(newAggregateEntity);
			}
		}

		for (MultiredditDTO multiredditDTO : multiredditList) {
			AggregateEntity aggregateToUpdate = aggregateMap.get(multiredditDTO.getMultireddit(), multiredditDTO.getSubreddit());
			if (aggregateToUpdate != null) {
				aggregateToUpdate.setIsSubredditAdded(true);
				aggregateToUpdate.setLastModified(sharedUtils.getCurrentTime());
				updatedAggregateEntities.add(aggregateToUpdate);
			} else {
				AggregateEntity newAggregateEntity = addMultiredditEntity(userId, multiredditDTO, startId);
				updatedAggregateEntities.add(newAggregateEntity);
			}
		}

		return updatedAggregateEntities;
	}

	private AggregateEntity addSubredditEntity(String userId, SubredditDTO subredditDTO, Long startId) {
		AggregateEntity newAggregateEntity = new AggregateEntity();
		int idLength = 50;
		String aggregateId = sharedUtils.generateAggregateId(idLength);

		newAggregateEntity.setAggregateId(aggregateId);
		newAggregateEntity.setUserId(userId);
		newAggregateEntity.setSubredditId(subredditDTO.getSubredditId());
		newAggregateEntity.setMultireddit("");
		newAggregateEntity.setSubreddit(subredditDTO.getSubreddit());
		newAggregateEntity.setIsSubredditAdded(true);
		newAggregateEntity.setStartId(startId);
		newAggregateEntity.setLastModified(sharedUtils.getCurrentTime());

		return newAggregateEntity;
	}

	private AggregateEntity addMultiredditEntity(String userId, MultiredditDTO multiredditDTO, Long startId) {
		AggregateEntity newAggregateEntity = new AggregateEntity();
		int idLength = 50;
		String aggregateId = sharedUtils.generateAggregateId(idLength);

		newAggregateEntity.setAggregateId(aggregateId);
		newAggregateEntity.setUserId(userId);
		newAggregateEntity.setMultiredditId(multiredditDTO.getMultiredditId());
		newAggregateEntity.setMultireddit(multiredditDTO.getMultireddit());
		newAggregateEntity.setSubreddit(multiredditDTO.getSubreddit());
		newAggregateEntity.setIsSubredditAdded(true);
		newAggregateEntity.setStartId(startId);
		newAggregateEntity.setLastModified(sharedUtils.getCurrentTime());

		return newAggregateEntity;
	}

}
