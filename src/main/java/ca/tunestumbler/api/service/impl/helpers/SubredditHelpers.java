package ca.tunestumbler.api.service.impl.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import ca.tunestumbler.api.exceptions.RedditAccountNotAuthenticatedException;
import ca.tunestumbler.api.exceptions.TooManyRequestsFailedException;
import ca.tunestumbler.api.exceptions.WebRequestFailedException;
import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditFetchResponseModel;

@Component
public class SubredditHelpers {
	
	@Autowired
	SharedUtils sharedUtils;

	public SubredditFetchResponseModel sendGetSubredditRequest(UserDTO user) {
		String token = user.getToken();
		if (token == null) {
			throw new RedditAccountNotAuthenticatedException(ErrorPrefixes.SUBREDDIT_SERVICE.getErrorPrefix()
					+ ErrorMessages.REDDIT_ACCOUNT_NOT_AUTHENTICATED.getErrorMessage());
		}

		String baseUrl = "https://oauth.reddit.com";
		String uri = "/subreddits/mine/subscriber";
		String userAgentHeader = "web:ca.tunestumbler.api:v1.0.0 (by /u/CrispiestHashbrown)";
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
						.map(clientResponse -> {
							if (clientResponse.statusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {
								throw new TooManyRequestsFailedException(ErrorPrefixes.SUBREDDIT_SERVICE.getErrorPrefix()
										+ ErrorMessages.TOO_MANY_REDDIT_REQUESTS.getErrorMessage() 
										+ clientResponse.headers().header("x-ratelimit-reset") + " seconds");
							} else if (clientResponse.statusCode().isError()) {
								throw new WebRequestFailedException(ErrorPrefixes.SUBREDDIT_SERVICE.getErrorPrefix()
										+ ErrorMessages.FAILED_EXTERNAL_WEB_REQUEST.getErrorMessage());
							}
							return clientResponse;
					    })
						.block()
						.bodyToMono(SubredditFetchResponseModel.class)
						.block();
	}
	
	public SubredditDTO createNewSubredditDTO(String subreddit, SubredditFetchResponseModel response) {
		SubredditDTO newSubredditDTO = new SubredditDTO();
		String subredditId = sharedUtils.generateSubredditId(50);

		newSubredditDTO.setSubredditId(subredditId);
		newSubredditDTO.setSubreddit(subreddit);
		newSubredditDTO.setAfterId(response.getData().getAfter());
		newSubredditDTO.setBeforeId(response.getData().getBefore());

		return newSubredditDTO;
	}

}
