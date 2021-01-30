package ca.tunestumbler.api.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.io.entity.AuthValidationEntity;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.service.AuthValidationService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.service.impl.helpers.AuthorizationHelpers;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.AuthValidationDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.auth.AuthResponseModel;

@Service
public class AuthValidationServiceImpl implements AuthValidationService {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	SharedUtils sharedUtils;

	@Autowired
	AuthorizationHelpers authorizationHelpers;

	@Override
	public AuthValidationDTO createAuthState(String userId) {
		AuthValidationEntity authValidationEntity = authorizationHelpers.createAuthValidationEntity(userId);

		AuthValidationDTO authValidationDTO = new AuthValidationDTO();
		BeanUtils.copyProperties(authValidationEntity, authValidationDTO);

		return authValidationDTO;
	}

	@Override
	public HttpHeaders createHandlerHeaders(String state, String code) {
		AuthValidationEntity authValidationEntity = authorizationHelpers.updateState(state, code);

		AuthResponseModel response = authorizationHelpers.createRedditTokens(code);
		String tokenLifetime = Integer.toString(response.getExpires_in());

		if (response.getAccess_token() != null && response.getRefresh_token() != null 
				&& authorizationHelpers.isScopesValid(response.getScope())) {
			UserDTO userDTO = userService.getUserByUserId(authValidationEntity.getUserId());
			userDTO.setToken(response.getAccess_token());
			userDTO.setRefreshToken(response.getRefresh_token());
			userDTO.setTokenLifetime(tokenLifetime);
			userService.voidUpdateUser(userDTO.getUserId(), userDTO);

			return authorizationHelpers.createResponseHeaders(tokenLifetime);
		}
		
		return new HttpHeaders();
	}

	@Override
	public HttpHeaders createRefreshTokenHeaders(String userId) {
		UserDTO userDTO = userService.getUserByUserId(userId);
		AuthResponseModel response = authorizationHelpers.refreshRedditToken(userDTO.getRefreshToken());
		String tokenLifetime = Integer.toString(response.getExpires_in());
		
		if (response.getAccess_token() != null && !response.getAccess_token().isEmpty() 
				&& authorizationHelpers.isScopesValid(response.getScope())) {
			userDTO.setToken(response.getAccess_token());
			userDTO.setTokenLifetime(tokenLifetime);
			userService.voidUpdateUser(userId, userDTO);
			
			return authorizationHelpers.createResponseHeaders(tokenLifetime);
		}

		return new HttpHeaders();
	}
	
}
