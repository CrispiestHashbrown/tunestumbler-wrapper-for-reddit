package ca.tunestumbler.api.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ca.tunestumbler.api.exceptions.SubredditServiceException;
import ca.tunestumbler.api.exceptions.UserServiceException;
import ca.tunestumbler.api.io.entity.SubredditEntity;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.SubredditRepository;
import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.service.SubredditService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditDataChildrenModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditFetchResponseModel;

@Service
public class SubredditServiceImpl implements SubredditService {

	@Autowired
	SubredditRepository subredditRepository;

	@Autowired
	UserService userService;

	@Autowired
	SharedUtils sharedUtils;

//	TODO: Get subreddits from paginated results
	@Override
	public List<SubredditDTO> fetchSubreddits(UserDTO user) {
		if (user == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		Long userMaxId = subredditRepository.findMaxIdByUserId(user.getUserId());
		Long maxId = subredditRepository.findMaxId();
		Long startId = sharedUtils.setStartId(userMaxId, maxId);

		SubredditFetchResponseModel response = sendGetSubredditRequest(user);
		List<SubredditDTO> subreddits = new ArrayList<>();
		List<SubredditDataChildrenModel> subredditModel = response.getData().getChildren();
		if (subredditModel == null || subredditModel.isEmpty() || 
				subredditModel.size() < 2 && subredditModel.get(0).getData().getDisplay_name_prefixed().equals("announcements")) {
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
			subredditEntity.setSubreddit(data.getData().getDisplay_name_prefixed());
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
		List<SubredditDTO> subreddits = new ArrayList<>();
		
		if (user == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		String userId = user.getUserId();
		Long startId = subredditRepository.findMaxStartIdByUserId(userId);
		List<SubredditEntity> subredditEntities = subredditRepository.findSubredditsByUserIdAndMaxStartId(userId, startId);
		for (SubredditEntity subredditEntity : subredditEntities) {
			subredditEntity.setIsSubscribed(false);
		}

		SubredditFetchResponseModel response = sendGetSubredditRequest(user);
		List<SubredditDataChildrenModel> subredditModel = response.getData().getChildren();
		if (subredditModel == null || subredditModel.isEmpty() || 
				subredditModel.size() < 2 && subredditModel.get(0).getData().getDisplay_name_prefixed().equals("announcements")) {
			subredditRepository.saveAll(subredditEntities);
			return subreddits;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		List<SubredditEntity> updatedSubredditEntities = new ArrayList<>();
		for (SubredditDataChildrenModel data : subredditModel) {
			String subreddit = data.getData().getDisplay_name_prefixed();
			SubredditEntity subredditEntity = subredditRepository.findByUserIdAndSubredditAndMaxStartId(userId, subreddit, startId);
			if (subredditEntity != null) {
				subredditEntity.setIsSubscribed(true);
				subredditEntity.setLastModified(sharedUtils.getCurrentTime());
				updatedSubredditEntities.add(subredditEntity);
			} else {
				SubredditEntity newSubredditEntity = new SubredditEntity();
				String subredditId = sharedUtils.generateSubredditId(50);

				newSubredditEntity.setSubredditId(subredditId);
				newSubredditEntity.setSubreddit(data.getData().getDisplay_name_prefixed());
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

	private SubredditFetchResponseModel sendGetSubredditRequest(UserDTO user) {
		String token = user.getToken();
		if (token == null) {
			throw new SubredditServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		String baseUrl = "https://oauth.reddit.com";
		String uri = "/subreddits/mine/subscriber";
		String userAgentHeader = "web:ca.tunestumbler.api:v0.0.1 (by /u/CrispiestHashbrown)";
		String authHeader = SecurityConstants.TOKEN_PREFIX + token;

		WebClient client = WebClient
				.builder()
					.baseUrl(baseUrl)
					.defaultHeader(HttpHeaders.USER_AGENT, userAgentHeader)
					.defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
		WebClient.UriSpec<WebClient.RequestBodySpec> request = client.method(HttpMethod.GET);
		WebClient.RequestBodySpec requestUri = request.uri(uri);

		return requestUri
				.exchange()
				.block()
				.bodyToMono(SubredditFetchResponseModel.class)
				.block();
	}

	@Override
	public List<SubredditDTO> getSubredditsByUserId(String userId) {
		List<SubredditDTO> existingSubreddits = new ArrayList<>();
		
		Long startId = subredditRepository.findMaxStartIdByUserId(userId);
		List<SubredditEntity> subredditList = subredditRepository.findSubredditsByUserIdAndMaxId(userId, startId);

		for (SubredditEntity subreddit : subredditList) {
			SubredditDTO subredditDTO = new SubredditDTO();
			BeanUtils.copyProperties(subreddit, subredditDTO);
			existingSubreddits.add(subredditDTO);
		}

		return existingSubreddits;
	}

}
