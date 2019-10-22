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
import ca.tunestumbler.api.io.entity.UserEntity;
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
		Long userMaxId = subredditRepository.findMaxIdByUserId(user.getUserId());
		Long maxId = subredditRepository.findMaxId();
		Long startId = sharedUtils.setStartId(userMaxId, maxId);

		SubredditFetchResponseModel response = subredditHelpers.sendGetSubredditRequest(user);
		List<SubredditDTO> subreddits = new ArrayList<>();
		List<SubredditDataChildrenModel> subredditModel = response.getData().getChildren();
		if (subredditModel == null || subredditModel.isEmpty() || subredditModel.size() < 2
				&& subredditModel.get(0).getData().getDisplay_name().equals("announcements")) {
			return subreddits;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		Boolean isSubscribed = true;
		List<SubredditEntity> subredditEntities = new ArrayList<>();
		for (SubredditDataChildrenModel data : subredditModel) {
			SubredditEntity subredditEntity = new SubredditEntity();
			String subredditId = sharedUtils.generateSubredditId(50);

			subredditEntity.setSubredditId(subredditId);
			subredditEntity.setSubreddit(data.getData().getDisplay_name());
			subredditEntity.setUserEntity(userEntity);
			subredditEntity.setUserId(userEntity.getUserId());
			subredditEntity.setAfterId(response.getData().getAfter());
			subredditEntity.setBeforeId(response.getData().getBefore());
			subredditEntity.setStartId(startId);
			subredditEntity.setIsSubscribed(isSubscribed);
			subredditEntity.setLastModified(sharedUtils.getCurrentTime());

			subredditEntities.add(subredditEntity);
		}

		List<SubredditEntity> storedSubredditEntities = subredditRepository.saveAll(subredditEntities);
		Type listType = new TypeToken<List<SubredditDTO>>() {
		}.getType();
		subreddits = new ModelMapper().map(storedSubredditEntities, listType);

		return subreddits;
	}

	@Override
	public List<SubredditDTO> updateSubreddits(UserDTO user) {
		String userId = user.getUserId();
		Long startId = subredditRepository.findMaxStartIdByUserId(userId);
		
//		Get subreddit entities and store in a hashmap
		List<SubredditEntity> subredditEntities = subredditRepository
				.findSubredditsByUserIdAndMaxStartIdAndSubscribed(userId, startId);
		HashMap<String, SubredditEntity> subredditMap = new HashMap<String, SubredditEntity>();
		for (SubredditEntity subredditEntity : subredditEntities) {
			subredditEntity.setIsSubscribed(false);
			subredditMap.put(subredditEntity.getSubreddit(), subredditEntity);
		}

//		Get user subreddits and handle the response
		SubredditFetchResponseModel response = subredditHelpers.sendGetSubredditRequest(user);
		List<SubredditDataChildrenModel> subredditModel = response.getData().getChildren();
		List<SubredditDTO> subreddits = new ArrayList<>();
		if (subredditModel == null || subredditModel.isEmpty() || subredditModel.size() < 2
				&& subredditModel.get(0).getData().getDisplay_name().equals("announcements")) {
			if (subredditEntities != null) {
				subredditRepository.saveAll(subredditEntities);
			}

			return subreddits;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

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
				SubredditEntity newSubredditEntity = new SubredditEntity();
				String subredditId = sharedUtils.generateSubredditId(50);

				newSubredditEntity.setSubredditId(subredditId);
				newSubredditEntity.setSubreddit(data.getData().getDisplay_name());
				newSubredditEntity.setUserEntity(userEntity);
				newSubredditEntity.setUserId(userEntity.getUserId());
				newSubredditEntity.setAfterId(response.getData().getAfter());
				newSubredditEntity.setBeforeId(response.getData().getBefore());
				newSubredditEntity.setStartId(startId);
				newSubredditEntity.setIsSubscribed(true);
				newSubredditEntity.setLastModified(sharedUtils.getCurrentTime());

				updatedSubredditEntities.add(newSubredditEntity);
			}
		}

		List<SubredditEntity> storedSubredditEntities = subredditRepository.saveAll(updatedSubredditEntities);
		Type listType = new TypeToken<List<SubredditDTO>>() {
		}.getType();
		subreddits = new ModelMapper().map(storedSubredditEntities, listType);

		return subreddits;
	}

	@Override
	public List<SubredditDTO> getSubredditsByUserId(UserDTO user) {
		String userId = user.getUserId();
		Long startId = subredditRepository.findMaxStartIdByUserId(userId);
		List<SubredditEntity> subredditList = subredditRepository.findSubredditsByUserIdAndMaxIdAndSubscribed(userId,
				startId);

		List<SubredditDTO> existingSubreddits = new ArrayList<>();
		if (subredditList == null) {
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
