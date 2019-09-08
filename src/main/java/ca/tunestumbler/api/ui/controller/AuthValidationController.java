package ca.tunestumbler.api.ui.controller;

import java.nio.charset.Charset;
import java.util.Base64;

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
import org.springframework.web.servlet.view.RedirectView;

import com.google.common.base.Strings;

import ca.tunestumbler.api.exceptions.MissingPathParametersException;
import ca.tunestumbler.api.exceptions.WebRequestFailedException;
import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.service.AuthValidationService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.AuthValidationDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.AuthResponseModel;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;

@RestController
@RequestMapping("/auth")
public class AuthValidationController {

	@Autowired
	SharedUtils sharedUtils;

	@Autowired
	AuthValidationService authValidationService;

	@Autowired
	UserService userService;
	
	@GetMapping(path = "/connect/{userId}")
	public RedirectView connectRedditAccount(@PathVariable String userId) {
		UserDTO userDTO = userService.getUserByUserId(userId);
		AuthValidationDTO authValidationDTO = authValidationService.createAuthState(userDTO);
		String stateId = authValidationDTO.getStateId();
		String url = "https://www.reddit.com/api/v1/authorize" +
			"?client_id=VvztT4RO6UUmAA" +
			"&response_type=code" +
			"&state=" + stateId +
			"&redirect_uri=http://localhost:8080/tunestumbler-wrapper-for-reddit/auth/handler/" +
			"&duration=permanent" +
			"&scope=read,history,vote,save,account,subscribe,mysubreddits";
		return new RedirectView(url);
	}

	@GetMapping(path = "/handler", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> validateStateAndRedirect(@RequestParam String state, @RequestParam String code) {
		if (Strings.isNullOrEmpty(state) || Strings.isNullOrEmpty(code)) {
			throw new MissingPathParametersException(ErrorPrefixes.AUTH_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		AuthValidationDTO authValidationDTO = authValidationService.getAuthState(state);

		if (authValidationDTO == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		AuthValidationDTO updatedAuthValidationDTO = authValidationService.updateState(state, code);

		String baseUrl = "https://www.reddit.com";
		String uri = "/api/v1/access_token";
		String userAgentHeader = "web:ca.tunestumbler.api:v0.0.1 (by /u/CrispiestHashbrown)";
		String creds = Base64.getEncoder().encodeToString(SecurityConstants.getAuth().getBytes());
		String authHeader = "Basic " + creds;
		String redirectUri = "http://localhost:8080/tunestumbler-wrapper-for-reddit/auth/handler/";

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
		String scopes = response.getScope();
		String validScopes = "account history mysubreddits read save subscribe vote";

		if (accessToken == null || refreshToken == null	|| !scopes.equals(validScopes)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			UserDTO userDTO = userService.getUserByUserId(updatedAuthValidationDTO.getUserId());

			userDTO.setToken(accessToken);
			userDTO.setRefreshToken(refreshToken);

			UserDTO updatedUserDTO = userService.updateUser(userDTO.getUserId(), userDTO);

			if (updatedUserDTO.getToken() == null && updatedUserDTO.getRefreshToken() == null) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
	}

	@GetMapping(path = "/refresh_token/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> getNewToken(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.AUTH_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		UserDTO userDTO = userService.getUserByUserId(userId);
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
		String scopes = response.getScope();
		String validScopes = "account history mysubreddits read save subscribe vote";

		if (accessToken == null || !scopes.equals(validScopes)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			userDTO.setToken(accessToken);
			UserDTO updatedUserDTO = userService.updateUser(userId, userDTO);

			if (updatedUserDTO.getToken() == null) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
	}

}
