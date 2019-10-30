package ca.tunestumbler.api.service.impl.helpers;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.security.AuthenticationFacade;
import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.auth.AuthResponseModel;
import ca.tunestumbler.api.exceptions.BadRequestException;
import ca.tunestumbler.api.exceptions.WebRequestFailedException;

@Component
public class AuthorizationHelpers {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthenticationFacade authenticationFacade;

	public void isAuthorized(String userId) {
		String username = userRepository.findEmailByUserId(userId);
		String authentication = authenticationFacade.getAuthentication().getName();
		if (!authentication.equals(username)) {
			throw new BadRequestException();
		}
	}

	public AuthResponseModel refreshRedditToken(String refreshToken) {
		String baseUrl = "https://www.reddit.com";
		String uri = "/api/v1/access_token";
		String userAgentHeader = "web:ca.tunestumbler.api:v0.0.1 (by /u/CrispiestHashbrown)";
		String creds = Base64.getEncoder().encodeToString(SecurityConstants.getAuth().getBytes());
		String authHeader = "Basic " + creds;

		WebClient client = WebClient
				.builder()
					.baseUrl(baseUrl)
					.defaultHeader(HttpHeaders.USER_AGENT, userAgentHeader)
					.defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
		WebClient.UriSpec<WebClient.RequestBodySpec> request = client.method(HttpMethod.POST);
		WebClient.RequestBodySpec requestUri = request.uri(uri);
		
		LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grant_type", "refresh_token");
		map.add("refresh_token", refreshToken);
		BodyInserter<MultiValueMap<String, Object>, ClientHttpRequest> inserter = BodyInserters.fromMultipartData(map);
		return requestUri
				.body(inserter)
					.acceptCharset(StandardCharsets.UTF_8)
				.exchange()
				.map(clientResponse -> {
					if (clientResponse.statusCode().isError()) {
						throw new WebRequestFailedException(ErrorPrefixes.AUTH_SERVICE.getErrorPrefix()
								+ ErrorMessages.FAILED_EXTERNAL_WEB_REQUEST.getErrorMessage());
					}

					return clientResponse;
			    })
				.block()
				.bodyToMono(AuthResponseModel.class)
				.block();
	}

	public AuthResponseModel createRedditTokens(String code) {
		String baseUrl = "https://www.reddit.com";
		String uri = "/api/v1/access_token";
		String userAgentHeader = "web:ca.tunestumbler.api:v0.0.1 (by /u/CrispiestHashbrown)";
		String creds = Base64.getEncoder().encodeToString(SecurityConstants.getAuth().getBytes());
		String authHeader = "Basic " + creds;
		String redirectUri = "https://www.tunestumbler.com/";
		WebClient client = WebClient
				.builder()
					.baseUrl(baseUrl)
					.defaultHeader(HttpHeaders.USER_AGENT, userAgentHeader)
					.defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
		WebClient.UriSpec<WebClient.RequestBodySpec> request = client.method(HttpMethod.POST);
		WebClient.RequestBodySpec requestUri = request.uri(uri);
		
		LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grant_type", "authorization_code");
		map.add("code", code);
		map.add("redirect_uri", redirectUri);
		BodyInserter<MultiValueMap<String, Object>, ClientHttpRequest> inserter = BodyInserters.fromMultipartData(map);
		return requestUri
				.body(inserter)
					.acceptCharset(StandardCharsets.UTF_8)
				.exchange()
				.map(clientResponse -> {
					if (clientResponse.statusCode().isError()) {
						throw new WebRequestFailedException(ErrorPrefixes.AUTH_SERVICE.getErrorPrefix()
								+ ErrorMessages.FAILED_EXTERNAL_WEB_REQUEST.getErrorMessage());
					}

					return clientResponse;
			    })
				.block()
				.bodyToMono(AuthResponseModel.class)
				.block();
	}

}
