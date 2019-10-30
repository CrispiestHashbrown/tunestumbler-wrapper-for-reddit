package ca.tunestumbler.api.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.base.Strings;

import ca.tunestumbler.api.exceptions.AuthValidationServiceException;
import ca.tunestumbler.api.exceptions.ResourceNotFoundException;
import ca.tunestumbler.api.exceptions.WebRequestFailedException;
import ca.tunestumbler.api.io.entity.AuthValidationEntity;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.AuthValidationRepository;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.service.AuthValidationService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.AuthValidationDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.auth.AuthResponseModel;

@Service
public class AuthValidationServiceImpl implements AuthValidationService {

	@Autowired
	AuthValidationService authValidationService;

	@Autowired
	UserService userService;
	
	@Autowired
	AuthValidationRepository authValidationRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public AuthValidationDTO createAuthState(UserDTO user) {
		UserEntity userEntity = userRepository.findByUserId(user.getUserId());

		if (userEntity == null) {
			throw new ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		String stateId = sharedUtils.generateStateId(50);
		String authorizationUrl = "https://www.reddit.com/api/v1/authorize" +
				"?client_id=VvztT4RO6UUmAA" +
				"&response_type=code" +
				"&state=" + stateId +
				"&redirect_uri=https://www.tunestumbler.com/" +
				"&duration=permanent" +
				"&scope=read,history,vote,save,account,subscribe,mysubreddits";
		
		AuthValidationEntity authValidationEntity = new AuthValidationEntity();

		authValidationEntity.setStateId(stateId);
		authValidationEntity.setUserId(userEntity.getUserId());
		authValidationEntity.setLastModified(sharedUtils.getCurrentTime());
		authValidationEntity.setAuthorizationUrl(authorizationUrl);

		AuthValidationEntity storedAuthValidation = authValidationRepository.save(authValidationEntity);

		AuthValidationDTO authValidationDTO = new AuthValidationDTO();
		BeanUtils.copyProperties(storedAuthValidation, authValidationDTO);

		return authValidationDTO;
	}

	@Override
	public AuthValidationDTO getAuthState(String stateId) {
		AuthValidationEntity authValidationEntity = authValidationRepository.findByStateIdAndValidated(stateId, false);

		if (authValidationEntity == null) {
			throw new AuthValidationServiceException(ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		AuthValidationDTO existingAuthValidation = new AuthValidationDTO();
		BeanUtils.copyProperties(authValidationEntity, existingAuthValidation);
		return existingAuthValidation;
	}

	@Override
	public AuthValidationDTO updateState(String stateId, String code) {
		if (Strings.isNullOrEmpty(stateId) || Strings.isNullOrEmpty(code)) {
			throw new AuthValidationServiceException(ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		AuthValidationDTO authValiationToUpdate = new AuthValidationDTO();

		AuthValidationEntity authValidationEntity = authValidationRepository.findByStateIdAndValidated(stateId, false);

		if (authValidationEntity == null) {
			throw new AuthValidationServiceException(ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		authValidationEntity.setValidated(true);
		authValidationEntity.setCode(code);
		authValidationEntity.setLastModified(sharedUtils.getCurrentTime());

		AuthValidationEntity updatedAuthValidation = authValidationRepository.save(authValidationEntity);
		BeanUtils.copyProperties(updatedAuthValidation, authValiationToUpdate);

		return authValiationToUpdate;
	}

	@Override
	public HttpHeaders createHandlerHeaders(String state, String code) {
		AuthResponseModel response = createRedditTokens(code);
		String accessToken = response.getAccess_token();
		String refreshToken = response.getRefresh_token();
		String validScopes = "account history mysubreddits read save subscribe vote";

		AuthValidationDTO updatedAuthValidationDTO = authValidationService.updateState(state, code);
		HttpHeaders responseHeaders = new HttpHeaders();
		if (accessToken != null && refreshToken != null	&& response.getScope().equals(validScopes)) {
			UserDTO userDTO = userService.getUserByUserId(updatedAuthValidationDTO.getUserId());

			userDTO.setToken(accessToken);
			userDTO.setRefreshToken(refreshToken);
			userDTO.setTokenLifetime(Integer.toString(response.getExpires_in()));

			List<String> exposedHeaders = new ArrayList<>();
			String redditLifetimeHeader = "Reddit-Lifetime";
			exposedHeaders.add(redditLifetimeHeader);
			responseHeaders.setAccessControlExposeHeaders(exposedHeaders);
			UserDTO updatedUser = userService.updateUser(userDTO.getUserId(), userDTO);
			responseHeaders.set(redditLifetimeHeader, updatedUser.getTokenLifetime());
		}
		
		return responseHeaders;
	}
	
	private AuthResponseModel createRedditTokens(String code) {
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
						throw new WebRequestFailedException(ErrorPrefixes.AUTH_CONTROLLER.getErrorPrefix()
								+ ErrorMessages.FAILED_EXTERNAL_WEB_REQUEST.getErrorMessage());
					}

					return clientResponse;
			    })
				.block()
				.bodyToMono(AuthResponseModel.class)
				.block();
	}

	@Override
	public HttpHeaders createRefreshTokenHeaders(String userId) {
		UserDTO userDTO = userService.getUserByUserId(userId);
		AuthResponseModel response = refreshRedditToken(userDTO.getRefreshToken());
		String accessToken = response.getAccess_token();
		String tokenLifetime = Integer.toString(response.getExpires_in());
		String validScopes = "account history mysubreddits read save subscribe vote";

		HttpHeaders responseHeaders = new HttpHeaders();
		if (accessToken != null && !accessToken.isEmpty() && response.getScope().equals(validScopes)) {
			userDTO.setToken(accessToken);
			userDTO.setTokenLifetime(tokenLifetime);
			userService.voidUpdateUser(userId, userDTO);

			List<String> exposedHeaders = new ArrayList<>();
			String redditLifetimeHeader = "Reddit-Lifetime";
			exposedHeaders.add(redditLifetimeHeader);
			responseHeaders.setAccessControlExposeHeaders(exposedHeaders);
			responseHeaders.set(redditLifetimeHeader, tokenLifetime);
		}

		return responseHeaders;
	}

	private AuthResponseModel refreshRedditToken(String refreshToken) {
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
						throw new WebRequestFailedException(ErrorPrefixes.AUTH_CONTROLLER.getErrorPrefix()
								+ ErrorMessages.FAILED_EXTERNAL_WEB_REQUEST.getErrorMessage());
					}

					return clientResponse;
			    })
				.block()
				.bodyToMono(AuthResponseModel.class)
				.block();
	}
	
}
