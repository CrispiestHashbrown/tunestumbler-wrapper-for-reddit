package ca.tunestumbler.api.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ca.tunestumbler.api.exceptions.FiltersNotFoundException;
import ca.tunestumbler.api.exceptions.RedditAccountNotAuthenticatedException;
import ca.tunestumbler.api.exceptions.WebRequestFailedException;
import ca.tunestumbler.api.io.entity.FiltersEntity;
import ca.tunestumbler.api.io.entity.ResultsEntity;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.FiltersRepository;
import ca.tunestumbler.api.io.repositories.ResultsRepository;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.service.ResultsService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.SortResultsById;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.ResultsResponseModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsDataChildrenDataModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsDataChildrenModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsFetchResponseModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsObjectResponseModel;
import ca.tunestumbler.api.io.repositories.specification.ResultsSpecification;

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
	public ResultsResponseModel fetchResults(UserDTO user, String orderBy) {
		String userId = user.getUserId();
		List<FiltersEntity> filters = filtersRepository.findFiltersByUserIdAndIsActive(userId);
		if (filters == null || filters.isEmpty()) {
			throw new FiltersNotFoundException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.FILTER_RESOURCES_NOT_FOUND.getErrorMessage());
		}

		String baseUrl = "https://oauth.reddit.com";
		String uri = filtersURIBuilder(filters, orderBy);
		ResultsFetchResponseModel response = sendGetResultsRequest(user, baseUrl, uri);
		List<ResultsDataChildrenModel> resultsModel = response.getData().getChildren();
		if (resultsModel == null || resultsModel.isEmpty()
				|| resultsModel.size() < 2 && resultsModel.get(0).getData().getStickied()) {
			return new ResultsResponseModel();
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		Long maxId = setResultsStartId(userId);
		String afterId = response.getData().getAfter();
		List<ResultsEntity> resultsEntities = new ArrayList<>();
		for (ResultsDataChildrenModel data : resultsModel) {
			ResultsEntity resultsEntity = addResultsEntity(userEntity, data.getData(), maxId);
			resultsEntities.add(resultsEntity);
		}

		resultsRepository.saveAll(resultsEntities);
		
		List<ResultsEntity> filteredResults = new ArrayList<>();
		for (FiltersEntity filtersEntity : filters) {
			filteredResults.addAll(getFilteredResults(filtersEntity, userId, maxId));
		}

		List<ResultsEntity> filteredResultsWithoutDuplicates = new ArrayList<>(new HashSet<>(filteredResults));
		Collections.sort(filteredResultsWithoutDuplicates, new SortResultsById());
		
		ResultsResponseModel results = new ResultsResponseModel();
		Type listType = new TypeToken<List<ResultsObjectResponseModel>>() {
		}.getType();
		List<ResultsObjectResponseModel> responseObject = new ModelMapper().map(filteredResultsWithoutDuplicates, listType);
		results.setResults(responseObject);
		results.setAfterId(afterId);
		results.setNextUri(uri);
		
		return results;
	}

	@Override
	public ResultsResponseModel fetchNextResults(UserDTO user, String nextUri, String afterId) {
		String baseUrl = "https://oauth.reddit.com";
		String fullUri = nextUri + afterIdPathVariableName + afterId;

		ResultsFetchResponseModel response = sendGetResultsRequest(user, baseUrl, fullUri);
		List<ResultsDataChildrenModel> resultsModel = response.getData().getChildren();
		if (resultsModel == null || resultsModel.isEmpty()
				|| resultsModel.size() < 2 && resultsModel.get(0).getData().getStickied()) {
			return new ResultsResponseModel();
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		String userId = user.getUserId();
		Long newStartId = setResultsStartId(userId);

		String nextAfterId = response.getData().getAfter();
		List<ResultsEntity> resultsEntities = new ArrayList<>();
		for (ResultsDataChildrenModel data : resultsModel) {
			ResultsEntity resultsEntity = addResultsEntity(userEntity, data.getData(), newStartId);
			resultsEntities.add(resultsEntity);
		}

		resultsRepository.saveAll(resultsEntities);
		
		List<ResultsEntity> filteredResults = new ArrayList<>();
		List<FiltersEntity> filters = filtersRepository.findFiltersByUserIdAndIsActive(userId);

		if (filters == null || filters.isEmpty()) {
			throw new FiltersNotFoundException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.FILTER_RESOURCES_NOT_FOUND.getErrorMessage());
		}

		for (FiltersEntity filtersEntity : filters) {
			filteredResults.addAll(getFilteredResults(filtersEntity, userId, newStartId));
		}

		List<ResultsEntity> filteredResultsWithoutDuplicates = new ArrayList<>(new HashSet<>(filteredResults));
		Collections.sort(filteredResultsWithoutDuplicates, new SortResultsById());
		
		ResultsResponseModel results = new ResultsResponseModel();
		Type listType = new TypeToken<List<ResultsObjectResponseModel>>() {
		}.getType();
		List<ResultsObjectResponseModel> responseObject = new ModelMapper().map(filteredResultsWithoutDuplicates, listType);
		results.setResults(responseObject);
		results.setNextUri(nextUri);
		results.setAfterId(nextAfterId);
		
		return results;
	}

	private List<ResultsEntity> getFilteredResults(FiltersEntity filtersEntity, String userId, Long startId) {
		return resultsRepository.findAll(
						Specification.where(ResultsSpecification.withUserId(userId))
						.and(ResultsSpecification.withStartId(startId))
						.and(ResultsSpecification.withSubreddit(filtersEntity.getSubreddit())
						.and(ResultsSpecification.withMinScore(filtersEntity.getMinScore()))
						.and(ResultsSpecification.withNSFW(filtersEntity.getAllowNSFWFlag()))
						.and(ResultsSpecification.withDomainOnly(setYoutubeFilterIfYoutube(filtersEntity.getShowByDomain())))
						.and(ResultsSpecification.withoutDomain(setYoutubeFilterIfYoutube(filtersEntity.getHideByDomain())))
						.and(ResultsSpecification.withTitleKeyword(filtersEntity.getShowByKeyword()))
						.and(ResultsSpecification.withoutTitleKeyword(filtersEntity.getHideByKeyword()))));
	}

	private String setYoutubeFilterIfYoutube (String domain) {
		String youtubeFilter = "youtu";
		if (domain.toLowerCase().contains(youtubeFilter)) {
			return youtubeFilter;
		} else {
			return domain;
		}
	}

	private ResultsFetchResponseModel sendGetResultsRequest(UserDTO user, String baseUrl, String uri) {
		String token = user.getToken();
		if (token == null || token.isEmpty()) {
			throw new RedditAccountNotAuthenticatedException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.REDDIT_ACCOUNT_NOT_AUTHENTICATED.getErrorMessage());
		}

		String userAgentHeader = "web:ca.tunestumbler.api:v0.0.1 (by /u/CrispiestHashbrown)";
		String authHeader = SecurityConstants.TOKEN_PREFIX + token;

		WebClient client = WebClient.builder().baseUrl(baseUrl).defaultHeader(HttpHeaders.USER_AGENT, userAgentHeader)
				.defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
		WebClient.UriSpec<WebClient.RequestBodySpec> request = client.method(HttpMethod.GET);
		WebClient.RequestBodySpec requestUri = request.uri(uri);

		return requestUri
						.exchange()
						.map(clientResponse -> {
							if (clientResponse.statusCode().isError()) {
								throw new WebRequestFailedException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
										+ ErrorMessages.FAILED_EXTERNAL_WEB_REQUEST.getErrorMessage());
							}

							return clientResponse;
					    })
						.block()
						.bodyToMono(ResultsFetchResponseModel.class)
						.block();
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

		String limit = "limit=60";
		StringBuilder uri = new StringBuilder("/");
		if (subreddits.length() != 0) {
			uri.append("r/").append(subreddits)
				.append("/").append(orderBy)
				.append("/?")
				.append(limit);
		}

		return uri.toString();
	}

	private ResultsEntity addResultsEntity(UserEntity userEntity, ResultsDataChildrenDataModel data, long startId) {
		ResultsEntity resultsEntity = new ResultsEntity();
		String resultsId = sharedUtils.generateResultsId(idLength);
		String userId = userEntity.getUserId();

		resultsEntity.setResultsId(resultsId);
		resultsEntity.setUserId(userId);
		resultsEntity.setSubreddit(data.getSubreddit());
		resultsEntity.setTitle(data.getTitle());
		resultsEntity.setScore(data.getScore());
		resultsEntity.setCreated(data.getCreated());
		resultsEntity.setCreatedUtc(data.getCreated_utc());
		resultsEntity.setDomain(data.getDomain());
		resultsEntity.setIsNsfw(data.getOver_18());
		resultsEntity.setIsSpoiler(data.getSpoiler());
		resultsEntity.setComments(data.getNum_comments());
		resultsEntity.setPermalink(data.getPermalink());
		resultsEntity.setIsStickied(data.getStickied());
		resultsEntity.setUrl(data.getUrl());
		resultsEntity.setStartId(startId);
		resultsEntity.setLastModified(sharedUtils.getCurrentTime());

		return resultsEntity;
	}

	private Long setResultsStartId(String userId) {
		Long userMaxId = resultsRepository.findMaxIdByUserId(userId);
		Long maxId = resultsRepository.findMaxId();
		return sharedUtils.setStartId(userMaxId, maxId);
	}

}
