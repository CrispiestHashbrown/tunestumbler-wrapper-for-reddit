package ca.tunestumbler.api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ca.tunestumbler.api.exceptions.FiltersNotFoundException;
import ca.tunestumbler.api.exceptions.RedditAccountNotAuthenticatedException;
import ca.tunestumbler.api.exceptions.TooManyRequestsFailedException;
import ca.tunestumbler.api.exceptions.WebRequestFailedException;
import ca.tunestumbler.api.io.entity.ExcludedDomainEntity;
import ca.tunestumbler.api.io.entity.ExcludedKeywordEntity;
import ca.tunestumbler.api.io.entity.FiltersEntity;
import ca.tunestumbler.api.io.entity.ResultsEntity;
import ca.tunestumbler.api.io.entity.SelectedDomainEntity;
import ca.tunestumbler.api.io.entity.SelectedKeywordEntity;
import ca.tunestumbler.api.io.repositories.FiltersRepository;
import ca.tunestumbler.api.io.repositories.ResultsRepository;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.service.ResultsService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.NextResultsRequestDTO;
import ca.tunestumbler.api.shared.dto.ResultsDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.shared.mapper.ResultsMapper;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.ResultsResponseModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsDataChildrenModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsFetchResponseModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsObjectResponseModel;
import ca.tunestumbler.api.io.repositories.specification.ResultsSpecification;
import ca.tunestumbler.api.io.repositories.specification.SearchCriteria;
import ca.tunestumbler.api.io.repositories.specification.SearchOperation;
import ca.tunestumbler.api.io.repositories.specification.SortResultsById;

@Service
public class ResultsServiceImpl implements ResultsService {

	@Autowired
	ResultsRepository resultsRepository;

	@Autowired
	FiltersRepository filtersRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	SharedUtils sharedUtils;

	@Autowired
	ResultsMapper resultsMapper;

	@Override
	public ResultsResponseModel fetchResults(UserDTO user, String orderBy) {
		String userId = user.getUserId();
		List<FiltersEntity> filters = filtersRepository.findFiltersByUserId(userId);
		if (filters == null || filters.isEmpty()) {
			throw new FiltersNotFoundException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.FILTER_RESOURCES_NOT_FOUND.getErrorMessage());
		}

		String uri = filtersUriBuilder(filters, orderBy);
		ResultsFetchResponseModel response = sendGetResultsRequest(user, uri);
		List<ResultsDataChildrenModel> resultsModel = response.getData().getChildren();
		if (resultsModel == null || resultsModel.isEmpty()
				|| resultsModel.size() < 2 && resultsModel.get(0).getData().getStickied()) {
			return new ResultsResponseModel();
		}

		List<ResultsDTO> resultDTOs = resultsMapper.resultsDataChildrenListToResultsDTOlist(resultsModel);
		Long maxId = setResultsStartId(userId);
		resultsRepository.saveAll(resultsMapper.resultsDTOlistToResultsEntityList(resultDTOs, userId, maxId));

		String afterId = response.getData().getAfter();
		List<ResultsObjectResponseModel> responseObjects = buildFilteredResultObjects(filters, maxId);
		return resultsMapper.buildResponseModel(responseObjects, afterId, uri);
	}

	@Override
	public ResultsResponseModel fetchNextResults(UserDTO user, NextResultsRequestDTO nextResultsRequestDTO) {
		String userId = user.getUserId();
		List<FiltersEntity> filters = filtersRepository.findFiltersByUserId(userId);
		if (filters == null || filters.isEmpty()) {
			throw new FiltersNotFoundException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.FILTER_RESOURCES_NOT_FOUND.getErrorMessage());
		}

		String fullUri = nextResultsRequestDTO.getNextUri() + "&after=" + nextResultsRequestDTO.getAfterId();
		ResultsFetchResponseModel response = sendGetResultsRequest(user, fullUri);
		List<ResultsDataChildrenModel> resultsModel = response.getData().getChildren();
		if (resultsModel == null || resultsModel.isEmpty()
				|| resultsModel.size() < 2 && resultsModel.get(0).getData().getStickied()) {
			return new ResultsResponseModel();
		}

		Long newStartId = setResultsStartId(userId);
		List<ResultsDTO> resultDTOs = resultsMapper.resultsDataChildrenListToResultsDTOlist(resultsModel);
		resultsRepository.saveAll(resultsMapper.resultsDTOlistToResultsEntityList(resultDTOs, userId, newStartId));

		String nextAfterId = response.getData().getAfter();
		List<ResultsObjectResponseModel> responseObjects = buildFilteredResultObjects(filters, newStartId);
		return resultsMapper.buildResponseModel(responseObjects, nextAfterId, nextResultsRequestDTO.getNextUri());
	}

	private String filtersUriBuilder(List<FiltersEntity> filters, String orderBy) {
		StringBuilder subreddits = new StringBuilder("");
		for (FiltersEntity filtersEntity : filters) {
			if (!filtersEntity.getSubreddit().isEmpty()) {
				if (subreddits.length() != 0) {
					subreddits.append("+");
				}
				subreddits.append(filtersEntity.getSubreddit());
			}
		}

		String limit = "limit=100";
		StringBuilder uri = new StringBuilder("/");
		if (subreddits.length() != 0) {
			uri.append("r/").append(subreddits).append("/").append(orderBy).append("/?").append(limit);
		}

		return uri.toString();
	}

	private ResultsFetchResponseModel sendGetResultsRequest(UserDTO user, String uri) {
		String token = user.getToken();
		if (token == null || token.isEmpty()) {
			throw new RedditAccountNotAuthenticatedException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.REDDIT_ACCOUNT_NOT_AUTHENTICATED.getErrorMessage());
		}

		String userAgentHeader = "web:ca.tunestumbler.api:v1.0.0 (by /u/CrispiestHashbrown)";
		String authHeader = SecurityConstants.TOKEN_PREFIX + token;
		String baseUrl = "https://oauth.reddit.com";

		WebClient client = WebClient.builder().baseUrl(baseUrl).defaultHeader(HttpHeaders.USER_AGENT, userAgentHeader)
				.defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
		WebClient.UriSpec<WebClient.RequestBodySpec> request = client.method(HttpMethod.GET);
		WebClient.RequestBodySpec requestUri = request.uri(uri);

		return requestUri.exchange().map(clientResponse -> {
			if (clientResponse.statusCode().equals(HttpStatus.UNAUTHORIZED)) {
				throw new RedditAccountNotAuthenticatedException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
						+ ErrorMessages.REDDIT_ACCOUNT_NOT_AUTHENTICATED.getErrorMessage());
			} else if (clientResponse.statusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {
				throw new TooManyRequestsFailedException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
						+ ErrorMessages.TOO_MANY_REDDIT_REQUESTS.getErrorMessage()
						+ clientResponse.headers().header("x-ratelimit-reset") + " seconds");
			} else if (clientResponse.statusCode().isError()) {
				throw new WebRequestFailedException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
						+ ErrorMessages.FAILED_EXTERNAL_WEB_REQUEST.getErrorMessage());
			}

			return clientResponse;
		}).block().bodyToMono(ResultsFetchResponseModel.class).block();
	}

	private Long setResultsStartId(String userId) {
		Long userMaxId = resultsRepository.findMaxIdByUserId(userId);
		Long maxId = resultsRepository.findMaxId();
		return sharedUtils.setStartId(userMaxId, maxId);
	}

	private List<ResultsObjectResponseModel> buildFilteredResultObjects(List<FiltersEntity> filters, long maxId) {
		List<ResultsEntity> filteredResultEntities = getFilteredResults(filters, maxId);
		List<ResultsDTO> filteredResultDTOs = resultsMapper.resultsEntityListToResultsDTOlist(filteredResultEntities);
		return resultsMapper.resultsDTOlistToResultsResponseObjectList(filteredResultDTOs);
	}

	private List<ResultsEntity> getFilteredResults(List<FiltersEntity> filters, Long startId) {
		List<ResultsEntity> filteredResults = new ArrayList<>();
		for (FiltersEntity filter : filters) {
			ResultsSpecification resultsSpec = new ResultsSpecification();
			createCommonResultsSpecification(filter, startId, resultsSpec);
			createDomainFilterResultsSpecification(filter, resultsSpec);
			filteredResults.addAll(resultsRepository.findAll(resultsSpec));
		}

		Collections.sort(filteredResults, new SortResultsById());
		return filteredResults;
	}

	private void createCommonResultsSpecification(FiltersEntity filter, Long startId,
			ResultsSpecification resultsSpec) {
		resultsSpec.add(new SearchCriteria(filter.getUserId(), "userId", SearchOperation.EQUAL));
		resultsSpec.add(new SearchCriteria(startId, "startId", SearchOperation.GREATER_THAN_OR_EQUAL));
		resultsSpec.add(new SearchCriteria(filter.getSubreddit(), "subreddit", SearchOperation.EQUAL));
		resultsSpec.add(new SearchCriteria(filter.getMinScore(), "score", SearchOperation.GREATER_THAN_OR_EQUAL));
		resultsSpec.add(new SearchCriteria(filter.getAllowNSFWFlag(), "isNsfw", SearchOperation.EQUAL));
		for (ExcludedKeywordEntity excludedKeyword : filter.getExcludedKeywords()) {
			resultsSpec
					.add(new SearchCriteria(excludedKeyword.getExcludedKeyword(), "title", SearchOperation.NOT_LIKE));
		}
		for (SelectedKeywordEntity selectedKeyword : filter.getSelectedKeywords()) {
			resultsSpec.add(new SearchCriteria(selectedKeyword.getSelectedKeyword(), "title", SearchOperation.LIKE));
		}
	}

	private void createDomainFilterResultsSpecification(FiltersEntity filter, ResultsSpecification resultsSpec) {
		for (ExcludedDomainEntity excludedDomain : filter.getExcludedDomains()) {
			resultsSpec.add(new SearchCriteria(setYoutubeFilterIfYoutubeDomain(excludedDomain.getExcludedDomain()),
					"domain", SearchOperation.NOT_LIKE));
		}
		for (SelectedDomainEntity selectedDomain : filter.getSelectedDomains()) {
			resultsSpec.add(new SearchCriteria(setYoutubeFilterIfYoutubeDomain(selectedDomain.getSelectedDomain()),
					"domain", SearchOperation.LIKE));
		}
	}

	private String setYoutubeFilterIfYoutubeDomain(String domain) {
		String youtubeDomain = "youtube.com";
		String youtubeShortDomain = "youtu.be";
		String youtubeFilter = "youtu";
		if (domain.toLowerCase().contains(youtubeDomain) || domain.toLowerCase().contains(youtubeShortDomain)) {
			return youtubeFilter;
		} else {
			return domain;
		}
	}

}
