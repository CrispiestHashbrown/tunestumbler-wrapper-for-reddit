package ca.tunestumbler.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ca.tunestumbler.api.exceptions.SubredditServiceException;
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
	public List<SubredditDTO> fetchAndUpdateSubreddits(UserDTO user) {
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

		SubredditFetchResponseModel response = requestUri
				.exchange()
				.block()
				.bodyToMono(SubredditFetchResponseModel.class)
				.block();

		Long userMaxId = subredditRepository.getMaxIdByUserId(user.getUserId());
		Long maxId = subredditRepository.getMaxId();
		Long startId = sharedUtils.setStartId(userMaxId, maxId);
		
		List<SubredditDTO> subreddits = new ArrayList<>();
		List<SubredditDataChildrenModel> subredditModel = response.getData().getChildren();
		if (subredditModel.size() < 2
				&& subredditModel.get(0).getData().getDisplay_name_prefixed().equals("announcements")
				|| subredditModel.isEmpty()) {
			return subreddits;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

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
			subredditEntity.setLastModified(sharedUtils.getCurrentTime());

			SubredditEntity storedSubreddit = subredditRepository.save(subredditEntity);

			SubredditDTO subredditDTO = new SubredditDTO();
			BeanUtils.copyProperties(storedSubreddit, subredditDTO);
			subreddits.add(subredditDTO);
		}

		return subreddits;
	}

	@Override
	public List<SubredditDTO> getSubredditsByUserId(String userId) {
		List<SubredditDTO> existingSubreddits = new ArrayList<>();
		
		Long startId = subredditRepository.getMaxStartIdByUserId(userId);
		List<SubredditEntity> subredditList = subredditRepository.findSubredditsByUserIdAndMaxId(userId, startId);

		for (SubredditEntity subreddit : subredditList) {
			SubredditDTO subredditDTO = new SubredditDTO();
			BeanUtils.copyProperties(subreddit, subredditDTO);
			existingSubreddits.add(subredditDTO);
		}

		return existingSubreddits;
	}

}
