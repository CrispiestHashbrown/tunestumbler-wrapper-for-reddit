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

import ca.tunestumbler.api.exceptions.ResultsServiceException;
import ca.tunestumbler.api.io.entity.FiltersEntity;
import ca.tunestumbler.api.io.entity.ResultsEntity;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.FiltersRepository;
import ca.tunestumbler.api.io.repositories.ResultsRepository;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.service.ResultsService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.ResultsDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.results.ResultsDataChildrenDataModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsDataChildrenModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsFetchResponseModel;

@Service
public class ResultsServiceImpl implements ResultsService {

	private int idLength = 50;
	private String afterIdPathVariableName = "&after=";
	
	@Autowired
	ResultsRepository resultsRepository;

	@Autowired
	FiltersRepository filtersRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public List<ResultsDTO> fetchResults(UserDTO user, String orderBy) {
		if (user == null) {
			throw new ResultsServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		String userId = user.getUserId();
		Long startId = filtersRepository.findMaxStartIdByUserId(userId);

		List<FiltersEntity> filters = filtersRepository.findFiltersByUserIdAndStartIdAndIsActive(userId, startId);
		List<ResultsDTO> results = new ArrayList<>();
		if (filters == null || filters.isEmpty()) {
			return results;
		}

		String baseUrl = "https://oauth.reddit.com";
		String uri = filtersURIBuilder(filters, orderBy);
		if (uri.isEmpty()) {
			return results;
		}
		
		ResultsFetchResponseModel response = sendGetResultsRequest(user, baseUrl, uri);
		List<ResultsDataChildrenModel> resultsModel = response.getData().getChildren();
		if (resultsModel == null || resultsModel.isEmpty()
				|| resultsModel.size() < 2 && resultsModel.get(0).getData().getStickied()) {
			return results;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		String afterId = response.getData().getAfter();
		List<ResultsEntity> resultsEntities = new ArrayList<>();
		for (ResultsDataChildrenModel data : resultsModel) {
			ResultsEntity resultsEntity = addResultsEntity(userEntity, uri, afterId, data.getData(), startId);
			resultsEntities.add(resultsEntity);
		}

		List<ResultsEntity> storedResultsEntities = resultsRepository.saveAll(resultsEntities);
		Type listType = new TypeToken<List<ResultsDTO>>() {
		}.getType();
		results = new ModelMapper().map(storedResultsEntities, listType);

		return results;
	}

	@Override
	public List<ResultsDTO> fetchNextResults(UserDTO user, String nextUri, String afterId) {
		if (user == null) {
			throw new ResultsServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		List<ResultsDTO> results = new ArrayList<>();
		if (afterId.isEmpty()) {
			return results;
		}

		String baseUrl = "https://oauth.reddit.com";
		String fullUri = nextUri + afterIdPathVariableName + afterId;

		ResultsFetchResponseModel response = sendGetResultsRequest(user, baseUrl, fullUri);
		List<ResultsDataChildrenModel> resultsModel = response.getData().getChildren();
		if (resultsModel == null || resultsModel.isEmpty()
				|| resultsModel.size() < 2 && resultsModel.get(0).getData().getStickied()) {
			return results;
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		String userId = user.getUserId();
		Long userMaxId = resultsRepository.findMaxIdByUserId(userId);
		Long maxId = resultsRepository.findMaxId();
		Long newStartId = sharedUtils.setStartId(userMaxId, maxId);

		String nextAfterId = response.getData().getAfter();
		List<ResultsEntity> resultsEntities = new ArrayList<>();
		for (ResultsDataChildrenModel data : resultsModel) {
			ResultsEntity resultsEntity = addResultsEntity(userEntity, nextUri, nextAfterId, data.getData(),
					newStartId);
			resultsEntities.add(resultsEntity);
		}

		List<ResultsEntity> storedResultsEntities = resultsRepository.saveAll(resultsEntities);
		Type listType = new TypeToken<List<ResultsDTO>>() {
		}.getType();
		results = new ModelMapper().map(storedResultsEntities, listType);

		return results;
	}

	private ResultsFetchResponseModel sendGetResultsRequest(UserDTO user, String baseUrl, String uri) {
		String token = user.getToken();
		if (token == null) {
			throw new ResultsServiceException(ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		String userAgentHeader = "web:ca.tunestumbler.api:v0.0.1 (by /u/CrispiestHashbrown)";
		String authHeader = SecurityConstants.TOKEN_PREFIX + token;

		WebClient client = WebClient.builder().baseUrl(baseUrl).defaultHeader(HttpHeaders.USER_AGENT, userAgentHeader)
				.defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
		WebClient.UriSpec<WebClient.RequestBodySpec> request = client.method(HttpMethod.GET);
		WebClient.RequestBodySpec requestUri = request.uri(uri);

		return requestUri.exchange().block().bodyToMono(ResultsFetchResponseModel.class).block();
	}

	private String filtersURIBuilder(List<FiltersEntity> filters, String orderBy) {
		StringBuilder subreddits = new StringBuilder("");
		for (FiltersEntity filtersEntity : filters) {
			String subreddit = filtersEntity.getSubreddit();
			if (!subreddit.isEmpty()) {
				if (subreddits.length() != 0) {
					subreddits.append("+");
				}
				subreddits.append(subreddit);
			}
		}

		String limit = "limit=100";
		StringBuilder uri = new StringBuilder("/");
		if (subreddits.length() != 0) {
			uri.append("r/").append(subreddits)
				.append("/").append(orderBy)
				.append("/?")
				.append(limit);
		}

		return uri.toString();
	}

	private ResultsEntity addResultsEntity(UserEntity userEntity, String nextUri, String afterId, ResultsDataChildrenDataModel data,
			long startId) {
		ResultsEntity resultsEntity = new ResultsEntity();
		String resultsId = sharedUtils.generateResultsId(idLength);
		String userId = userEntity.getUserId();

		resultsEntity.setResultsId(resultsId);
		resultsEntity.setUserEntity(userEntity);
		resultsEntity.setUserId(userId);
		resultsEntity.setSubreddit(data.getSubreddit());
		resultsEntity.setTitle(data.getTitle());
		resultsEntity.setScore(data.getScore());
		resultsEntity.setCreated(data.getCreated());
		resultsEntity.setCreatedUtc(data.getCreated_utc());
		resultsEntity.setDomain(data.getDomain());
		resultsEntity.setIsNsfw(data.getOver_18());
		resultsEntity.setIsSpoiler(data.getSpoiler());
		resultsEntity.setPermalink(data.getPermalink());
		resultsEntity.setIsStickied(data.getStickied());
		resultsEntity.setUrl(data.getUrl());
		resultsEntity.setNextUri(nextUri);
		resultsEntity.setAfterId(afterId);
		resultsEntity.setStartId(startId);
		resultsEntity.setLastModified(sharedUtils.getCurrentTime());

		return resultsEntity;
	}

}
