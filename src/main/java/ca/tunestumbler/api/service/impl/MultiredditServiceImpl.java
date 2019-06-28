package ca.tunestumbler.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ca.tunestumbler.api.exceptions.MultiredditServiceException;
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
	public List<MultiredditDTO> fetchAndUpdateMultireddits(UserDTO user) {
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

		MultiredditFetchResponseModel[] response = requestUri
				.exchange()
				.block()
				.bodyToMono(MultiredditFetchResponseModel[].class)
				.block();

		Long userMaxId = multiredditRepository.getMaxIdByUserId(user.getUserId());
		Long maxId = multiredditRepository.getMaxId();
		Long startId = 1L;
		if (userMaxId != null) {
			startId = userMaxId + 1;
		} else {
			if (maxId != null) {
				startId = maxId + 1;
			}
		}

		List<MultiredditDTO> multireddits = new ArrayList<>();
		List<MultiredditFetchResponseModel> multiredditModel = Arrays.asList(response);
		if (multiredditModel.isEmpty()) {
			return multireddits;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		for (MultiredditFetchResponseModel multireddit : multiredditModel) {
			for (MultiredditDataSubredditModel subreddit : multireddit.getData().getSubreddits()) {
				MultiredditEntity multiredditEntity = new MultiredditEntity();
				String multiredditId = sharedUtils.generateStateId(50);

				multiredditEntity.setMultiredditId(multiredditId);
				multiredditEntity.setMultireddit(multireddit.getData().getName());
				multiredditEntity.setSubreddit(subreddit.getName());
				multiredditEntity.setUserEntity(userEntity);
				multiredditEntity.setUserId(userEntity.getUserId());
				multiredditEntity.setStartId(startId);
				multiredditEntity.setLastModified(sharedUtils.getCurrentTime());

				MultiredditEntity storedMultireddit = multiredditRepository.save(multiredditEntity);

				MultiredditDTO multiredditDTO = new MultiredditDTO();
				BeanUtils.copyProperties(storedMultireddit, multiredditDTO);
				multireddits.add(multiredditDTO);
			}
		}

		return multireddits;
	}

	@Override
	public List<MultiredditDTO> getMultiredditsByUserId(String userId) {
		List<MultiredditDTO> existingMultireddits = new ArrayList<>();

		Long startId = multiredditRepository.getMaxStartIdByUserId(userId);
		List<MultiredditEntity> multiredditList = multiredditRepository.findSubredditsByUserIdAndMaxId(userId, startId);

		for (MultiredditEntity multireddit : multiredditList) {
			MultiredditDTO multiredditDTO = new MultiredditDTO();
			BeanUtils.copyProperties(multireddit, multiredditDTO);
			existingMultireddits.add(multiredditDTO);
		}

		return existingMultireddits;
	}

}
