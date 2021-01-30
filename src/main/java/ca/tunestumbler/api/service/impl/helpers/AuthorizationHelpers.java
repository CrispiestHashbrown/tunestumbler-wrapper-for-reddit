package ca.tunestumbler.api.service.impl.helpers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

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

import com.google.common.base.Strings;

import ca.tunestumbler.api.io.entity.AuthValidationEntity;
import ca.tunestumbler.api.io.repositories.AuthValidationRepository;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.security.AuthenticationFacade;
import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.auth.AuthResponseModel;
import ca.tunestumbler.api.exceptions.AuthValidationServiceException;
import ca.tunestumbler.api.exceptions.BadRequestException;
import ca.tunestumbler.api.exceptions.WebRequestFailedException;

@Component
public class AuthorizationHelpers {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthValidationRepository authValidationRepository;

	@Autowired
	AuthenticationFacade authenticationFacade;
	
	@Autowired
	SharedUtils sharedUtils;

	public String getUserIdFromAuth() {
		String authenticationEmail = authenticationFacade.getAuthentication().getName();
		return userRepository.findUserIdByEmail(authenticationEmail);
	}

	public void isAuthorized(String userId) {
		String username = userRepository.findEmailByUserId(userId);
		String authentication = authenticationFacade.getAuthentication().getName();
		if (!authentication.equals(username)) {
			throw new BadRequestException();
		}
	}

	public AuthValidationEntity createAuthValidationEntity(String userId) {
		String stateId = sharedUtils.generateStateId(50);
		String authorizationUrl = AuthConstants.BASE_URL + AuthConstants.AUTH_URI
				+ "?" + AuthConstants.CLIENT_ID_PARAMETER
				+ "&" + AuthConstants.RESPONSE_TYPE_PARAMETER
				+ "&" + AuthConstants.STATE_PARAMETER + stateId
				+ "&" + AuthConstants.REDIRECT_URI_PARAMETER + AuthConstants.REDIRECT_URI
				+ "&" + AuthConstants.DURATION_PARAMETER
				+ "&" + AuthConstants.SCOPE_PARAMETER;

		AuthValidationEntity authValidationEntity = new AuthValidationEntity();
		
		authValidationEntity.setStateId(stateId);
		authValidationEntity.setUserId(userId);
		authValidationEntity.setLastModified(sharedUtils.getCurrentTime());
		authValidationEntity.setAuthorizationUrl(authorizationUrl);

		authValidationRepository.save(authValidationEntity);

		return authValidationEntity;
	}
	
	public AuthResponseModel createRedditTokens(String code) {
		String creds = Base64.getEncoder().encodeToString(SecurityConstants.getAuth().getBytes());
		String authHeader = AuthConstants.AUTH_HEADER + creds;
		WebClient client = WebClient
				.builder()
					.baseUrl(AuthConstants.BASE_URL)
					.defaultHeader(HttpHeaders.USER_AGENT, AuthConstants.USER_AGENT_HEADER)
					.defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
		WebClient.UriSpec<WebClient.RequestBodySpec> request = client.method(HttpMethod.POST);
		WebClient.RequestBodySpec requestUri = request.uri(AuthConstants.ACCESS_TOKEN_URI);
		
		LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add(AuthConstants.GRANT_TYPE_HEADER, AuthConstants.AUTH_CODE_GRANT);
		map.add(AuthConstants.CODE_HEADER, code);
		map.add(AuthConstants.REDIRECT_URI_HEADER, AuthConstants.REDIRECT_URI);
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
	
	public Boolean isScopesValid(String scopes) {
		return scopes.equals(AuthConstants.VALID_SCOPES);
	}
	
	public HttpHeaders createResponseHeaders(String tokenLifetime) {
		HttpHeaders responseHeaders = new HttpHeaders();
		List<String> exposedHeaders = new ArrayList<>(Arrays.asList(AuthConstants.REDDIT_LIFETIME_HEADER));
		responseHeaders.setAccessControlExposeHeaders(exposedHeaders);
		responseHeaders.set(AuthConstants.REDDIT_LIFETIME_HEADER, tokenLifetime);
		return responseHeaders;
	}

	public AuthValidationEntity updateState(String stateId, String code) {
		if (Strings.isNullOrEmpty(stateId) || Strings.isNullOrEmpty(code)) {
			throw new AuthValidationServiceException(ErrorPrefixes.AUTH_SERVICE.getErrorPrefix()
					+ ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		AuthValidationEntity authValidationEntity = authValidationRepository.findByStateIdAndValidated(stateId, false);

		if (authValidationEntity == null) {
			throw new AuthValidationServiceException(ErrorPrefixes.AUTH_SERVICE.getErrorPrefix()
					+ ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		authValidationEntity.setValidated(true);
		authValidationEntity.setCode(code);
		authValidationEntity.setLastModified(sharedUtils.getCurrentTime());
		authValidationRepository.save(authValidationEntity);

		return authValidationEntity;
	}
	
	public AuthResponseModel refreshRedditToken(String refreshToken) {
		String creds = Base64.getEncoder().encodeToString(SecurityConstants.getAuth().getBytes());
		String authHeader = AuthConstants.AUTH_HEADER + creds;

		WebClient client = WebClient
				.builder()
					.baseUrl(AuthConstants.BASE_URL)
					.defaultHeader(HttpHeaders.USER_AGENT, AuthConstants.USER_AGENT_HEADER)
					.defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
		WebClient.UriSpec<WebClient.RequestBodySpec> request = client.method(HttpMethod.POST);
		WebClient.RequestBodySpec requestUri = request.uri(AuthConstants.ACCESS_TOKEN_URI);
		
		LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add(AuthConstants.GRANT_TYPE_HEADER, AuthConstants.REFRESH_TOKEN_GRANT);
		map.add(AuthConstants.REFRESH_TOKEN_HEADER, refreshToken);
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
