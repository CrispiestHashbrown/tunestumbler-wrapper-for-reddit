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

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

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

//		Get multireddit subreddits and create maps for multiredditEntities and subreddits
		List<MultiredditEntity> multiredditEntities = multiredditRepository
				.findSubredditsByUserIdAndMaxStartIdAndCurated(userId, startId);
		Table<String, String, MultiredditEntity> multiredditMap = HashBasedTable.create();
		for (MultiredditEntity multiredditEntity : multiredditEntities) {
			multiredditEntity.setIsCurated(false);
			multiredditMap.put(multiredditEntity.getMultireddit(), multiredditEntity.getSubreddit(), multiredditEntity);
		}

//		Get user multireddit subreddits and handle the response
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

//		Add new or update existing multireddits/subreddits
		Boolean isCurated = true;
		List<MultiredditEntity> updatedMultiredditEntities = new ArrayList<>();
		for (MultiredditFetchResponseModel multiredditData : multiredditModel) {
			String multireddit = multiredditData.getData().getName();
			for (MultiredditDataSubredditModel subredditData : multiredditData.getData().getSubreddits()) {
				String subreddit = subredditData.getName();
				MultiredditEntity existingMultiredditEntity = multiredditMap.get(multireddit, subreddit);

//				If the multireddit/subreddit exists, update it; otherwise, create a new record
				if (existingMultiredditEntity != null) {
					existingMultiredditEntity.setIsCurated(isCurated);
					existingMultiredditEntity.setLastModified(sharedUtils.getCurrentTime());
					updatedMultiredditEntities.add(existingMultiredditEntity);
				} else {
					MultiredditEntity multiredditEntityToAdd = new MultiredditEntity();
					String multiredditId = sharedUtils.generateMultiredditId(50);

					multiredditEntityToAdd.setMultiredditId(multiredditId);
					multiredditEntityToAdd.setMultireddit(multireddit);
					multiredditEntityToAdd.setSubreddit(subredditData.getName());
					multiredditEntityToAdd.setUserEntity(userEntity);
					multiredditEntityToAdd.setUserId(userEntity.getUserId());
					multiredditEntityToAdd.setStartId(startId);
					multiredditEntityToAdd.setIsCurated(isCurated);
					multiredditEntityToAdd.setLastModified(sharedUtils.getCurrentTime());
					updatedMultiredditEntities.add(multiredditEntityToAdd);
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
		if (multiredditList == null || multiredditList.isEmpty()) {
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
