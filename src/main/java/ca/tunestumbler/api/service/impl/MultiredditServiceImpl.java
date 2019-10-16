package ca.tunestumbler.api.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.io.entity.MultiredditEntity;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.MultiredditRepository;
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
	MultiredditRepository multiredditRepository;

	@Autowired
	MultiredditHelpers multiredditHelpers;
	
	@Autowired
	SharedUtils sharedUtils;

	@Override
	public List<MultiredditDTO> fetchMultireddits(UserDTO user) {
		Long userMaxId = multiredditRepository.findMaxIdByUserId(user.getUserId());
		Long maxId = multiredditRepository.findMaxId();
		Long startId = sharedUtils.setStartId(userMaxId, maxId);

		MultiredditFetchResponseModel[] response = multiredditHelpers.sendGetMultiredditRequest(user);
		List<MultiredditDTO> multireddits = new ArrayList<>();
		List<MultiredditFetchResponseModel> multiredditModel = Arrays.asList(response);
		if (multiredditModel.isEmpty()) {
			return multireddits;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		Boolean isCurated = true;
		List<MultiredditEntity> multiredditEntities = new ArrayList<>();
		for (MultiredditFetchResponseModel multireddit : multiredditModel) {
			for (MultiredditDataSubredditModel subreddit : multireddit.getData().getSubreddits()) {
				MultiredditEntity multiredditEntity = new MultiredditEntity();
				String multiredditId = sharedUtils.generateMultiredditId(50);

				multiredditEntity.setMultiredditId(multiredditId);
				multiredditEntity.setMultireddit(multireddit.getData().getName());
				multiredditEntity.setSubreddit(subreddit.getName());
				multiredditEntity.setUserEntity(userEntity);
				multiredditEntity.setUserId(userEntity.getUserId());
				multiredditEntity.setStartId(startId);
				multiredditEntity.setIsCurated(isCurated);
				multiredditEntity.setLastModified(sharedUtils.getCurrentTime());

				multiredditEntities.add(multiredditEntity);
			}
		}

		List<MultiredditEntity> storedMultiredditEntities = multiredditRepository.saveAll(multiredditEntities);
		Type listType = new TypeToken<List<MultiredditDTO>>() {
		}.getType();
		multireddits = new ModelMapper().map(storedMultiredditEntities, listType);

		return multireddits;
	}

	@Override
	public List<MultiredditDTO> updateMultireddits(UserDTO user) {
		String userId = user.getUserId();
		Long startId = multiredditRepository.findMaxStartIdByUserId(userId);
		List<MultiredditEntity> multiredditEntities = multiredditRepository
				.findSubredditsByUserIdAndMaxStartIdAndCurated(userId, startId);

		for (MultiredditEntity multiredditEntity : multiredditEntities) {
			multiredditEntity.setIsCurated(false);
		}

		MultiredditFetchResponseModel[] response = multiredditHelpers.sendGetMultiredditRequest(user);
		List<MultiredditFetchResponseModel> multiredditModel = Arrays.asList(response);
		List<MultiredditDTO> multireddits = new ArrayList<>();
		if (multiredditModel.isEmpty()) {
			if (multiredditEntities != null) {
				multiredditRepository.saveAll(multiredditEntities);
			}

			return multireddits;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		Boolean isCurated = true;
		List<MultiredditEntity> updatedMultiredditEntities = new ArrayList<>();
		for (MultiredditFetchResponseModel multiredditData : multiredditModel) {
			String multireddit = multiredditData.getData().getName();
//			TODO: turn this into multiredditEntities for loop
			List<MultiredditEntity> multiredditDataEntity = multiredditRepository
					.findByUserIdAndMultiredditAndMaxStartId(userId, multireddit, startId);
			for (MultiredditDataSubredditModel subredditData : multiredditData.getData().getSubreddits()) {
				String subreddit = subredditData.getName();
//				TODO: turn this into multiredditEntities for loop
				MultiredditEntity subredditDataEntity = multiredditRepository
						.findByUserIdAndSubredditAndMaxStartId(userId, subreddit, startId);
				if (multiredditDataEntity != null && subredditDataEntity != null) {

					subredditDataEntity.setIsCurated(isCurated);
					subredditDataEntity.setLastModified(sharedUtils.getCurrentTime());
					updatedMultiredditEntities.add(subredditDataEntity);
				}

				if (multiredditDataEntity == null && subredditDataEntity == null) {
					MultiredditEntity newMultiredditEntity = new MultiredditEntity();
					String multiredditId = sharedUtils.generateMultiredditId(50);

					newMultiredditEntity.setMultiredditId(multiredditId);
					newMultiredditEntity.setMultireddit(multiredditData.getData().getName());
					newMultiredditEntity.setSubreddit(subredditData.getName());
					newMultiredditEntity.setUserEntity(userEntity);
					newMultiredditEntity.setUserId(userEntity.getUserId());
					newMultiredditEntity.setStartId(startId);
					newMultiredditEntity.setIsCurated(isCurated);
					newMultiredditEntity.setLastModified(sharedUtils.getCurrentTime());

					updatedMultiredditEntities.add(newMultiredditEntity);
				}
			}
		}

		List<MultiredditEntity> storedMultiredditEntities = multiredditRepository.saveAll(updatedMultiredditEntities);
		Type listType = new TypeToken<List<MultiredditDTO>>() {
		}.getType();
		multireddits = new ModelMapper().map(storedMultiredditEntities, listType);

		return multireddits;
	}

	@Override
	public List<MultiredditDTO> getMultiredditsByUserId(UserDTO user) {
		String userId = user.getUserId();
		Long startId = multiredditRepository.findMaxStartIdByUserId(userId);
		List<MultiredditEntity> multiredditList = multiredditRepository
				.findSubredditsByUserIdAndMaxStartIdAndCurated(userId, startId);

		List<MultiredditDTO> existingMultireddits = new ArrayList<>();
		if (multiredditList == null) {
			return existingMultireddits;
		}

		for (MultiredditEntity multireddit : multiredditList) {
			MultiredditDTO multiredditDTO = new MultiredditDTO();
			BeanUtils.copyProperties(multireddit, multiredditDTO);
			existingMultireddits.add(multiredditDTO);
		}

		return existingMultireddits;
	}

}
