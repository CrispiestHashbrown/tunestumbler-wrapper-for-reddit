package ca.tunestumbler.api.service.impl.helpers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import ca.tunestumbler.api.exceptions.RedditAccountNotAuthenticatedException;
import ca.tunestumbler.api.exceptions.WebRequestFailedException;
import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditFetchResponseModel;

@Component
public class SubredditHelpers {
	public SubredditFetchResponseModel sendGetSubredditRequest(UserDTO user) {
		String token = user.getToken();
		if (token == null) {
			throw new RedditAccountNotAuthenticatedException(ErrorPrefixes.SUBREDDIT_SERVICE.getErrorPrefix()
					+ ErrorMessages.REDDIT_ACCOUNT_NOT_AUTHENTICATED.getErrorMessage());
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
						.map(clientResponse -> {
							if (clientResponse.statusCode().isError()) {
								throw new WebRequestFailedException(ErrorPrefixes.SUBREDDIT_SERVICE.getErrorPrefix()
										+ ErrorMessages.FAILED_EXTERNAL_WEB_REQUEST.getErrorMessage());
							}

							return clientResponse;
					    })
						.block()
						.bodyToMono(SubredditFetchResponseModel.class)
						.block();
	}
}
