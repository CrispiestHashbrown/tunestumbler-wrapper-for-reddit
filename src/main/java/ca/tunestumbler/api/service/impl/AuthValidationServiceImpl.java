package ca.tunestumbler.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import ca.tunestumbler.api.exceptions.AuthValidationServiceException;
import ca.tunestumbler.api.exceptions.ResourceNotFoundException;
import ca.tunestumbler.api.io.entity.AuthValidationEntity;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.AuthValidationRepository;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.service.AuthValidationService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.service.impl.helpers.AuthorizationHelpers;
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

	@Autowired
	AuthorizationHelpers authorizationHelpers;

	private Set<String> validScope = new HashSet<>(
			Arrays.asList("account", "read", "mysubreddits", "subscribe", "vote", "save", "history"));

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
	public AuthValidationDTO updateState(String stateId, String code) {
		if (Strings.isNullOrEmpty(stateId) || Strings.isNullOrEmpty(code)) {
			throw new AuthValidationServiceException(
					ErrorPrefixes.AUTH_SERVICE.getErrorPrefix() + ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		AuthValidationDTO authValiationToUpdate = new AuthValidationDTO();

		AuthValidationEntity authValidationEntity = authValidationRepository.findByStateIdAndValidated(stateId, false);

		if (authValidationEntity == null) {
			throw new AuthValidationServiceException(
					ErrorPrefixes.AUTH_SERVICE.getErrorPrefix() + ErrorMessages.BAD_REQUEST.getErrorMessage());
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
		return createHandlerHeadersHelper(state, code, validScope);
	}

	@Override
	public HttpHeaders createRefreshTokenHeaders(String userId) {
		return createRefreshTokenHeadersHelper(userId, validScope);
	}

	private HttpHeaders createHandlerHeadersHelper(String state, String code, Set<String> validScope) {
		AuthResponseModel response = authorizationHelpers.createRedditTokens(code);
		String accessToken = response.getAccess_token();
		String refreshToken = response.getRefresh_token();

		AuthValidationDTO updatedAuthValidationDTO = authValidationService.updateState(state, code);
		HttpHeaders responseHeaders = new HttpHeaders();
		if (accessToken != null && refreshToken != null && isScopeValid(validScope, response.getScope())) {
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

	private HttpHeaders createRefreshTokenHeadersHelper(String userId, Set<String> validScope) {
		UserDTO userDTO = userService.getUserByUserId(userId);
		AuthResponseModel response = authorizationHelpers.refreshRedditToken(userDTO.getRefreshToken());
		String accessToken = response.getAccess_token();
		String tokenLifetime = Integer.toString(response.getExpires_in());

		HttpHeaders responseHeaders = new HttpHeaders();
		if (accessToken != null && !accessToken.isEmpty() && isScopeValid(validScope, response.getScope())) {
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
	
	private boolean isScopeValid(Set<String> validScopes, String responseScope) {
		boolean isValid = true;
		String[] scopeToCheck = responseScope.split(" ");
		for (String scope : scopeToCheck) {
			if (!validScopes.contains(scope)) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

}
