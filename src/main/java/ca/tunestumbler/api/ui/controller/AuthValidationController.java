package ca.tunestumbler.api.ui.controller;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Base64;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.tunestumbler.api.security.SecurityConstants;
import ca.tunestumbler.api.service.AuthValidationService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.AuthValidationDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

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
	public ResponseEntity<Void> connectRedditAccount(@PathVariable String userId) {
		UserDTO userDTO = userService.getUserByUserId(userId);
		AuthValidationDTO authValidationDTO = authValidationService.createAuthState(userDTO);

		String stateId = authValidationDTO.getStateId();
		String url = "https://www.reddit.com/api/v1/authorize" +
			"?client_id=VvztT4RO6UUmAA" +
			"&response_type=code" +
			"&state=" + stateId +
			"&redirect_uri=http://localhost:8080/auth/handler/" +
			"&duration=permanent" +
			"&scope=read,history,vote,save,account,subscribe,mysubreddits";
		HttpHeaders headers = new HttpHeaders();
		headers.add("User-Agent", "web:ca.tunestumbler.api:v0.0.1 (by /u/CrispiestHashbrown)");
		return ResponseEntity.status(HttpStatus.FOUND)
				.location(URI.create(url))
				.headers(headers).build();
	}

	@GetMapping(path = "/handler", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> validateStateAndRedirect(@RequestParam String stateId, @RequestParam String code) throws Exception {
		AuthValidationDTO authValidationDTO = authValidationService.getAuthState(stateId);

		if (authValidationDTO == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		AuthValidationDTO updatedAuthValidationDTO = authValidationService.updateState(stateId, code,
				authValidationDTO);
		BeanUtils.copyProperties(updatedAuthValidationDTO, authValidationDTO);

		String baseUrl = "https://www.reddit.com";
		String uri = "/api/v1/access_token";
		String userAgentHeader = "web:ca.tunestumbler.api:v0.0.1 (by /u/CrispiestHashbrown";
		String creds = Base64.getEncoder().encodeToString(SecurityConstants.getAuth().getBytes());
		String authHeader = "Basic " + creds;
		String redirectUri = "http://localhost:8080/auth/handler/";

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
		ResponseEntity<?> response = requestUri
				.body(inserter)
					.acceptCharset(Charset.forName("UTF-8"))
				.exchange()
				.block()
				.bodyToMono(ResponseEntity.class)
				.block();

		if (response.getStatusCode() != HttpStatus.OK) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode responseBody = objectMapper.readTree(response.getBody().toString());
			String token = responseBody.get("access_token").asText();
			String refreshToken = responseBody.get("refresh_token").asText();

			String scope = responseBody.get("scope").asText();
			String validScopes = "account history mysubreddits read save subscribe vote";
			if (!scope.equals(validScopes)) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			UserDTO userDTO = updatedAuthValidationDTO.getUserDTO();
			userDTO.setToken(token);
			userDTO.setRefreshToken(refreshToken);

			UserDTO updatedUserDTO = userService.updateUser(userDTO.getUserId(), userDTO);

			if (updatedUserDTO.getToken() == null && updatedUserDTO.getRefreshToken() == null) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
	}

}
