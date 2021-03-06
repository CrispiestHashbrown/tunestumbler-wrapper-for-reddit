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
import ca.tunestumbler.api.exceptions.NoResultsFoundForNonexistingSubredditException;
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
import ca.tunestumbler.api.shared.dto.ResultsResponseDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.shared.mapper.ResultsMapper;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.results.ResultsDataChildrenModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsFetchResponseModel;
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
	public ResultsResponseDTO fetchResults(UserDTO user, String orderBy) {
		String userId = user.getUserId();
		List<FiltersEntity> filters = filtersRepository.findFiltersByUserId(userId);
		if (filters == null || filters.isEmpty()) {
			throw new FiltersNotFoundException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.FILTER_RESOURCES_NOT_FOUND.getErrorMessage());
		}

		String uri = filtersUriBuilder(filters, orderBy);
		ResultsFetchResponseModel response = sendGetResultsRequest(user, uri);
		Long maxId = setResultsStartId(userId);
		persistRedditSearchResults(response.getData().getChildren(), userId, maxId);

		return buildFilteredResultObjects(filters, maxId, response.getData().getAfter(), uri);
	}

	@Override
	public ResultsResponseDTO fetchNextResults(UserDTO user, NextResultsRequestDTO nextResultsRequestDTO) {
		String userId = user.getUserId();
		List<FiltersEntity> filters = filtersRepository.findFiltersByUserId(userId);
		if (filters == null || filters.isEmpty()) {
			throw new FiltersNotFoundException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.FILTER_RESOURCES_NOT_FOUND.getErrorMessage());
		}

		String fullUri = nextResultsRequestDTO.getNextUri() + "&after=" + nextResultsRequestDTO.getAfterId();
		ResultsFetchResponseModel response = sendGetResultsRequest(user, fullUri);
		Long newStartId = setResultsStartId(userId);
		persistRedditSearchResults(response.getData().getChildren(), userId, newStartId);

		return buildFilteredResultObjects(filters, newStartId, response.getData().getAfter(),
				nextResultsRequestDTO.getNextUri());
	}

	@Override
	public List<String> fetchYoutubePlaylists(UserDTO user) {
		String userId = user.getUserId();
		List<FiltersEntity> filters = filtersRepository.findFiltersByUserId(userId);
		if (filters == null || filters.isEmpty()) {
			throw new FiltersNotFoundException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.FILTER_RESOURCES_NOT_FOUND.getErrorMessage());
		}

		return generatePlaylists(filters, user);
	}

	private Long setResultsStartId(String userId) {
		Long userMaxId = resultsRepository.findMaxIdByUserId(userId);
		Long maxId = resultsRepository.findMaxId();
		return sharedUtils.setStartId(userMaxId, maxId);
	}

	private ResultsResponseDTO buildFilteredResultObjects(List<FiltersEntity> filters, long maxId, String afterId,
			String uri) {
		List<ResultsEntity> filteredResultEntities = getFilteredResults(filters, maxId);
		List<ResultsDTO> filteredResultDTOs = resultsMapper.resultsEntityListToResultsDTOlist(filteredResultEntities);
		return resultsMapper.resultsDTOlistToResultsResponseDTO(filteredResultDTOs, afterId, uri);
	}

	private List<String> generatePlaylists(List<FiltersEntity> filters, UserDTO user) {
		double playlistCount = 10;
		int playlistSize = 50;

		int numOfSubredditsPerSearch;
		int numOfFilteredResultsPerSearch;
		if (filters.size() <= playlistCount) {
			numOfSubredditsPerSearch = 1;
			numOfFilteredResultsPerSearch = (int) (playlistCount / filters.size()) * playlistSize;
		} else {
			numOfSubredditsPerSearch = (int) Math.ceil(filters.size() / playlistCount);
			numOfFilteredResultsPerSearch = playlistSize;
		}

		List<String> playlists = new ArrayList<>();
		Long startId = setResultsStartId(user.getUserId());
		for (int count = 0; count < filters.size(); count += numOfSubredditsPerSearch) {
			List<FiltersEntity> filtersGroup = filters.subList(count, count + numOfSubredditsPerSearch);
			String uri = filtersUriBuilder(filtersGroup, "hot", "youtu");
			ResultsFetchResponseModel response = sendGetResultsRequest(user, uri);
			if (response == null) {
				return new ArrayList<>();
			}
			persistRedditSearchResults(response.getData().getChildren(), user.getUserId(), startId);
			getAndPersistNextRedditSearchResults(response, user, filtersGroup, startId, numOfFilteredResultsPerSearch,
					uri);

			List<String> playlistIds = getFilteredResultIdsForPlaylists(filtersGroup, startId);
			playlists.addAll(createFilterGroupPlaylists(playlistIds, numOfFilteredResultsPerSearch, playlistSize));
		}

		return playlists;
	}

	private String filtersUriBuilder(List<FiltersEntity> filters, String orderBy) {
		StringBuilder subreddits = subredditUriBuilder(filters);
		String limit = "limit=100";
		StringBuilder uri = new StringBuilder("/");
		if (subreddits.length() != 0) {
			uri.append("r/").append(subreddits).append("/").append(orderBy).append("/?").append(limit);
		}

		return uri.toString();
	}

	private String filtersUriBuilder(List<FiltersEntity> filters, String orderBy, String domainParameter) {
		StringBuilder subreddits = subredditUriBuilder(filters);
		String limit = "&limit=100";
		String redditSearchApiUri = "/search/?restrict_sr=on" + limit + "&sort=";
		String domainQuery = "&q=site:";
		StringBuilder uri = new StringBuilder("/");
		if (subreddits.length() != 0) {
			uri.append("r/").append(subreddits).append(redditSearchApiUri).append(orderBy).append(domainQuery)
					.append(domainParameter);
		}

		return uri.toString();
	}

	private StringBuilder subredditUriBuilder(List<FiltersEntity> filters) {
		StringBuilder subreddits = new StringBuilder("");
		for (FiltersEntity filtersEntity : filters) {
			if (!filtersEntity.getSubreddit().isEmpty()) {
				if (subreddits.length() != 0) {
					subreddits.append("+");
				}
				subreddits.append(filtersEntity.getSubreddit());
			}
		}

		return subreddits;
	}

	private List<ResultsEntity> getFilteredResults(List<FiltersEntity> filters, Long startId) {
		List<ResultsEntity> filteredResults = new ArrayList<>();
		for (FiltersEntity filter : filters) {
			ResultsSpecification resultsSpec = createCommonResultsSpecification(filter, startId);
			createDomainFilterResultsSpecification(filter, resultsSpec);
			filteredResults.addAll(resultsRepository.findAll(resultsSpec));
		}

		Collections.sort(filteredResults, new SortResultsById());
		return filteredResults;
	}

	private void persistRedditSearchResults(List<ResultsDataChildrenModel> resultsModel, String userId, Long maxId) {
		List<ResultsDTO> resultDTOs = resultsMapper.resultsDataChildrenListToResultsDTOlist(resultsModel);
		if (!resultDTOs.isEmpty()) {
			resultsRepository.saveAll(resultsMapper.resultsDTOlistToResultsEntityList(resultDTOs, userId, maxId));
		}
	}

	private void getAndPersistNextRedditSearchResults(ResultsFetchResponseModel response, UserDTO user,
			List<FiltersEntity> filtersGroup, Long startId, int numOfFilteredResultsPerSearch, String uri) {
		String afterId = response.getData().getAfter();
		if (getFilteredResultsCount(filtersGroup, startId) < numOfFilteredResultsPerSearch) {
			for (int idCount = 0; idCount < numOfFilteredResultsPerSearch
					|| afterId == null; idCount = getFilteredResultsCount(filtersGroup, startId)) {
				String nextUri = uri + "&after=" + afterId;
				ResultsFetchResponseModel nextResponse = sendGetResultsRequest(user, nextUri);
				if (nextResponse == null) {
					break;
				}

				afterId = nextResponse.getData().getAfter();
				persistRedditSearchResults(nextResponse.getData().getChildren(), user.getUserId(), startId);
			}
		}
	}

	private List<String> getFilteredResultIdsForPlaylists(List<FiltersEntity> filters, Long startId) {
		List<ResultsEntity> filteredResults = new ArrayList<>();
		for (FiltersEntity filter : filters) {
			ResultsSpecification resultsSpec = createCommonResultsSpecification(filter, startId);
			filteredResults.addAll(resultsRepository.findAll(resultsSpec));
		}

		Collections.sort(filteredResults, new SortResultsById());

		return generateYoutubeIds(filteredResults);
	}

	private List<String> createFilterGroupPlaylists(List<String> playlistIds, int numOfFilteredResultsPerSearch,
			int playlistSize) {
		List<String> playlists = new ArrayList<>();
		StringBuilder playlistUrl = new StringBuilder();
		playlistUrl.append("http://www.youtube.com/watch_videos?video_ids=");
		int defaultUrlLength = playlistUrl.length();
		for (int id = 0; id <= numOfFilteredResultsPerSearch; id++) {
			if (id > 0 && id % playlistSize == 0) {
				playlists.add(playlistUrl.toString());
				playlistUrl.setLength(defaultUrlLength);
			}
			playlistUrl.append(playlistIds.get(id) + ",");
		}
		return playlists;
	}

	private int getFilteredResultsCount(List<FiltersEntity> filters, Long startId) {
		int count = 0;
		for (FiltersEntity filter : filters) {
			count += resultsRepository.count(createCommonResultsSpecification(filter, startId));
		}

		return count;
	}

	private ResultsSpecification createCommonResultsSpecification(FiltersEntity filter, Long startId) {
		ResultsSpecification resultsSpec = new ResultsSpecification();
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

		return resultsSpec;
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

	private ResultsFetchResponseModel sendGetResultsRequest(UserDTO user, String uri) {
		String token = user.getToken();
		if (token == null || token.isEmpty()) {
			throw new RedditAccountNotAuthenticatedException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.REDDIT_ACCOUNT_NOT_AUTHENTICATED.getErrorMessage());
		}

		String userAgentHeader = "web:ca.tunestumbler.api:v1.0.0 (by /u/CrispiestHashbrown)";
		String authHeader = SecurityConstants.TOKEN_PREFIX + token;
		String baseUrl = "https://oauth.reddit.com";

		return WebClient.builder().baseUrl(baseUrl).defaultHeader(HttpHeaders.USER_AGENT, userAgentHeader)
				.defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build().method(HttpMethod.GET)
				.uri(uri).exchange().map(clientResponse -> {
					if (clientResponse.statusCode().equals(HttpStatus.FOUND)) {
						throw new NoResultsFoundForNonexistingSubredditException(
								ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
								+ ErrorMessages.NO_SEARCH_REULTS_FOUND_FOR_NON_EXISTING_SUBREDDITS.getErrorMessage()
								+ "url: " + baseUrl + uri);
					} else if (clientResponse.statusCode().equals(HttpStatus.UNAUTHORIZED)) {
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

	private List<String> generateYoutubeIds(List<ResultsEntity> filteredResults) {
		List<String> ids = new ArrayList<>();
		String domainPrecursor = "v=";
		int youtubeIdLength = 11;
		String youtubeShortDomain = "youtu.be/";
		for (ResultsEntity entity : filteredResults) {
			if (entity.getUrl().contains(youtubeShortDomain)) {
				ids.add(entity.getUrl().substring(
						entity.getUrl().indexOf(youtubeShortDomain) + youtubeShortDomain.length(),
						entity.getUrl().indexOf(youtubeShortDomain) + youtubeShortDomain.length() + youtubeIdLength));
			} else {
				ids.add(entity.getUrl().substring(entity.getUrl().indexOf(domainPrecursor) + domainPrecursor.length(),
						entity.getUrl().indexOf(domainPrecursor) + domainPrecursor.length() + youtubeIdLength));
			}
		}

		return ids;
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
