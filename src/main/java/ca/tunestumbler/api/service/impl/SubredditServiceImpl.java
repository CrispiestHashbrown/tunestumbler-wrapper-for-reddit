package ca.tunestumbler.api.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.io.entity.SubredditEntity;
import ca.tunestumbler.api.io.repositories.SubredditRepository;
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
	SubredditRepository subredditRepository;

	@Autowired
	SubredditHelpers subredditHelpers;
	
	@Autowired
	SharedUtils sharedUtils;

//	TODO: Get subreddits from paginated results
	@Override
	public List<SubredditDTO> fetchSubreddits(UserDTO user) {
		String userId = user.getUserId();
		Long userMaxId = subredditRepository.findMaxIdByUserId(userId);
		Long maxId = subredditRepository.findMaxId();
		Long startId = sharedUtils.setStartId(userMaxId, maxId);

		SubredditFetchResponseModel response = subredditHelpers.sendGetSubredditRequest(user);
		List<SubredditDataChildrenModel> subredditModel = response.getData().getChildren();
		if (subredditModel == null || subredditModel.isEmpty()) {
			return new ArrayList<>();
		}

		List<SubredditEntity> subredditEntities = new ArrayList<>();
		for (SubredditDataChildrenModel data : subredditModel) {
			String subreddit = data.getData().getDisplay_name();
			SubredditEntity subredditEntity = 
					subredditHelpers.createNewSubredditEntity(userId, startId, subreddit, response);
			subredditEntities.add(subredditEntity);
		}

		List<SubredditEntity> storedSubredditEntities = subredditRepository.saveAll(subredditEntities);
		Type listType = new TypeToken<List<SubredditDTO>>() {
		}.getType();
		return new ModelMapper().map(storedSubredditEntities, listType);
	}

	@Override
	public List<SubredditDTO> updateSubreddits(UserDTO user) {
		String userId = user.getUserId();
		Long startId = subredditRepository.findMaxStartIdByUserId(userId);
		
//		Get subreddit entities and store in a hashmap
		List<SubredditEntity> subredditEntities = subredditRepository
				.findSubredditsByUserIdAndMaxStartIdAndSubscribed(userId, startId);
		HashMap<String, SubredditEntity> subredditMap = new HashMap<>();
		for (SubredditEntity subredditEntity : subredditEntities) {
			subredditEntity.setIsSubscribed(false);
			subredditMap.put(subredditEntity.getSubreddit(), subredditEntity);
		}

//		Get user subreddits and handle the response
		SubredditFetchResponseModel response = subredditHelpers.sendGetSubredditRequest(user);
		List<SubredditDataChildrenModel> subredditModel = response.getData().getChildren();
		if (subredditModel == null || subredditModel.isEmpty()) {
			if (subredditEntities != null) {
				subredditRepository.saveAll(subredditEntities);
			}

			return new ArrayList<>();
		}

//		Add new and update existing subreddits
		List<SubredditEntity> updatedSubredditEntities = new ArrayList<>();
		for (SubredditDataChildrenModel data : subredditModel) {
			String subreddit = data.getData().getDisplay_name();
			SubredditEntity subredditEntity = subredditMap.get(subreddit);
			if (subredditEntity != null) {
				subredditEntity.setIsSubscribed(true);
				subredditEntity.setLastModified(sharedUtils.getCurrentTime());
				updatedSubredditEntities.add(subredditEntity);
			} else {
				SubredditEntity newSubredditEntity = 
						subredditHelpers.createNewSubredditEntity(userId, startId, subreddit, response);
				updatedSubredditEntities.add(newSubredditEntity);
			}
		}

		List<SubredditEntity> storedSubredditEntities = subredditRepository.saveAll(updatedSubredditEntities);
		Type listType = new TypeToken<List<SubredditDTO>>() {
		}.getType();
		return new ModelMapper().map(storedSubredditEntities, listType);
	}

	@Override
	public List<SubredditDTO> getSubredditsByUserId(UserDTO user) {
		String userId = user.getUserId();
		Long startId = subredditRepository.findMaxStartIdByUserId(userId);
		List<SubredditEntity> subredditList = subredditRepository.findSubredditsByUserIdAndMaxIdAndSubscribed(userId,
				startId);

		List<SubredditDTO> existingSubreddits = new ArrayList<>();
		if (subredditList == null || subredditList.isEmpty()) {
			return existingSubreddits;
		}

		for (SubredditEntity subreddit : subredditList) {
			SubredditDTO subredditDTO = new SubredditDTO();
			BeanUtils.copyProperties(subreddit, subredditDTO);
			existingSubreddits.add(subredditDTO);
		}

		return existingSubreddits;
	}
}
