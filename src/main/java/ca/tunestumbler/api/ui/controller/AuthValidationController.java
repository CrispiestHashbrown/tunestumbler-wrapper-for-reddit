package ca.tunestumbler.api.ui.controller;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.base.Strings;

import ca.tunestumbler.api.exceptions.MissingPathParametersException;
import ca.tunestumbler.api.exceptions.WebRequestFailedException;
import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.service.AuthValidationService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.service.impl.helpers.AuthorizationHelpers;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.AuthValidationDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.auth.AuthConnectResponseModel;
import ca.tunestumbler.api.ui.model.response.auth.AuthResponseModel;

@RestController
@RequestMapping("/auth")
public class AuthValidationController {

	@Autowired
	SharedUtils sharedUtils;

	@Autowired
	AuthValidationService authValidationService;

	@Autowired
	UserService userService;
	
	@Autowired
	AuthorizationHelpers authorizationHelpers;

	@GetMapping(path = "/connect/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> connectRedditAccount(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.AUTH_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		UserDTO userDTO = userService.getUserByUserId(userId);
		AuthValidationDTO authValidationDTO = authValidationService.createAuthState(userDTO);
		
		AuthConnectResponseModel authConnectResponseModel = new AuthConnectResponseModel();
		BeanUtils.copyProperties(authValidationDTO, authConnectResponseModel);
		
		return new ResponseEntity<>(authConnectResponseModel, HttpStatus.CREATED);
	}

	@GetMapping(path = "/handler", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> validateStateAndRedirect(@RequestParam String state, @RequestParam String code) {
		if (Strings.isNullOrEmpty(state) || Strings.isNullOrEmpty(code)) {
			throw new MissingPathParametersException(ErrorPrefixes.AUTH_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		AuthValidationDTO authValidationDTO = authValidationService.getAuthState(state);

		if (authValidationDTO != null) {
			AuthValidationDTO updatedAuthValidationDTO = authValidationService.updateState(state, code);
	
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
			map.add("code", updatedAuthValidationDTO.getCode());
			map.add("redirect_uri", redirectUri);
			BodyInserter<MultiValueMap<String, Object>, ClientHttpRequest> inserter = BodyInserters.fromMultipartData(map);
			AuthResponseModel response = requestUri
					.body(inserter)
						.acceptCharset(Charset.forName("UTF-8"))
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
	
			String accessToken = response.getAccess_token();
			String refreshToken = response.getRefresh_token();
			String tokenLifetime = Integer.toString(response.getExpires_in());
			String scopes = response.getScope();
			String validScopes = "account history mysubreddits read save subscribe vote";

			if (accessToken != null && refreshToken != null	&& scopes.equals(validScopes)) {
				UserDTO userDTO = userService.getUserByUserId(updatedAuthValidationDTO.getUserId());
	
				userDTO.setToken(accessToken);
				userDTO.setRefreshToken(refreshToken);
				userDTO.setTokenLifetime(tokenLifetime);
	
				UserDTO updatedUser = userService.updateUser(userDTO.getUserId(), userDTO);
				
				HttpHeaders responseHeaders = new HttpHeaders();
				List<String> exposedHeaders = new ArrayList<>();
				String redditLifetimeHeader = "Reddit-Lifetime";
				exposedHeaders.add(redditLifetimeHeader);
				responseHeaders.setAccessControlExposeHeaders(exposedHeaders);
				responseHeaders.set(redditLifetimeHeader, updatedUser.getTokenLifetime());
				return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
			}
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping(path = "/refresh_token/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> getNewToken(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.AUTH_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		UserDTO userDTO = userService.getUserByUserId(userId);

		if (userDTO.getRefreshToken() == null || userDTO.getRefreshToken().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
			
		String refreshToken = userDTO.getRefreshToken();
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
		AuthResponseModel response = requestUri
				.body(inserter)
					.acceptCharset(Charset.forName("UTF-8"))
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

		String accessToken = response.getAccess_token();
		String tokenLifetime = Integer.toString(response.getExpires_in());
		String scopes = response.getScope();
		String validScopes = "account history mysubreddits read save subscribe vote";

		if (accessToken == null || accessToken.isEmpty() || !scopes.equals(validScopes)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			userDTO.setToken(accessToken);
			userDTO.setTokenLifetime(tokenLifetime);
			userService.voidUpdateUser(userId, userDTO);

			HttpHeaders responseHeaders = new HttpHeaders();
			List<String> exposedHeaders = new ArrayList<>();
			String redditLifetimeHeader = "Reddit-Lifetime";
			exposedHeaders.add(redditLifetimeHeader);
			responseHeaders.setAccessControlExposeHeaders(exposedHeaders);
			responseHeaders.set(redditLifetimeHeader, tokenLifetime);
			return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
		}
	}
	
}
