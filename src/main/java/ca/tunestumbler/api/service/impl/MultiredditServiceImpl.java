package ca.tunestumbler.api.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
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

import ca.tunestumbler.api.exceptions.MultiredditServiceException;
import ca.tunestumbler.api.exceptions.UserServiceException;
import ca.tunestumbler.api.io.entity.MultiredditEntity;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.MultiredditRepository;
import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.service.MultiredditService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.MultiredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditFetchResponseModel;
import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditDataSubredditModel;

@Service
public class MultiredditServiceImpl implements MultiredditService {

	@Autowired
	MultiredditRepository multiredditRepository;
	
	@Autowired
	UserService userService;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public List<MultiredditDTO> fetchMultireddits(UserDTO user) {
		if (user == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		Long userMaxId = multiredditRepository.findMaxIdByUserId(user.getUserId());
		Long maxId = multiredditRepository.findMaxId();
		Long startId = sharedUtils.setStartId(userMaxId, maxId);
		
		MultiredditFetchResponseModel[] response = sendGetMultiredditRequest(user);
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
		List<MultiredditDTO> multireddits = new ArrayList<>();
		
		if (user == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		String userId = user.getUserId();
		Long startId = multiredditRepository.findMaxStartIdByUserId(userId);
		List<MultiredditEntity> multiredditEntities = multiredditRepository.findSubredditsByUserIdAndMaxStartIdAndCurated(userId, startId);
		for (MultiredditEntity multiredditEntity : multiredditEntities) {
			multiredditEntity.setIsCurated(false);
		}

		MultiredditFetchResponseModel[] response = sendGetMultiredditRequest(user);
		List<MultiredditFetchResponseModel> multiredditModel = Arrays.asList(response);
		if (multiredditModel.isEmpty()) {
			multiredditRepository.saveAll(multiredditEntities);
			return multireddits;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		Boolean isCurated = true;
		List<MultiredditEntity> updatedMultiredditEntities = new ArrayList<>();
		for (MultiredditFetchResponseModel multiredditData : multiredditModel) {
			String multireddit = multiredditData.getData().getName();
			List<MultiredditEntity> multiredditDataEntity = multiredditRepository
					.findByUserIdAndMultiredditAndMaxStartId(userId, multireddit, startId);
			for (MultiredditDataSubredditModel subredditData : multiredditData.getData().getSubreddits()) {
				String subreddit = subredditData.getName();
				MultiredditEntity subredditDataEntity = multiredditRepository
						.findByUserIdAndSubredditAndMaxStartId(userId, subreddit, startId);
				if (multiredditDataEntity != null && subredditDataEntity != null) {

					subredditDataEntity.setIsCurated(isCurated);
					subredditDataEntity.setLastModified(sharedUtils.getCurrentTime());
					updatedMultiredditEntities.add(subredditDataEntity);
				}

				if (multiredditDataEntity == null || subredditDataEntity == null) {
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

	private MultiredditFetchResponseModel[] sendGetMultiredditRequest(UserDTO user) {
		String token = user.getToken();
		if (token == null) {
			throw new MultiredditServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		String baseUrl = "https://oauth.reddit.com";
		String uri = "/api/multi/mine";
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
				.bodyToMono(MultiredditFetchResponseModel[].class)
				.block();
	}

	@Override
	public List<MultiredditDTO> getMultiredditsByUserId(String userId) {
		List<MultiredditDTO> existingMultireddits = new ArrayList<>();

		Long startId = multiredditRepository.findMaxStartIdByUserId(userId);
		List<MultiredditEntity> multiredditList = multiredditRepository
				.findSubredditsByUserIdAndMaxStartIdAndCurated(userId, startId);
		for (MultiredditEntity multireddit : multiredditList) {
			MultiredditDTO multiredditDTO = new MultiredditDTO();
			BeanUtils.copyProperties(multireddit, multiredditDTO);
			existingMultireddits.add(multiredditDTO);
		}

		return existingMultireddits;
	}

}
